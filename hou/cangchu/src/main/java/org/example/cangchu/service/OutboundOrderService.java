package org.example.cangchu.service;

import lombok.extern.slf4j.Slf4j;
import org.example.cangchu.constant.SystemConstants;
import org.example.cangchu.dto.OutboundOrderDTO;
import org.example.cangchu.dto.OutboundOrderDetailDTO;
import org.example.cangchu.entity.Inventory;
import org.example.cangchu.entity.Material;
import org.example.cangchu.entity.OutboundOrder;
import org.example.cangchu.entity.OutboundOrderDetail;
import org.example.cangchu.entity.Warehouse;
import org.example.cangchu.enums.ErrorCode;
import org.example.cangchu.enums.OrderStatus;
import org.example.cangchu.exception.BusinessException;
import org.example.cangchu.repository.InventoryRepository;
import org.example.cangchu.repository.MaterialRepository;
import org.example.cangchu.repository.OutboundOrderDetailRepository;
import org.example.cangchu.repository.OutboundOrderRepository;
import org.example.cangchu.repository.WarehouseRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 出库单服务类
 * 提供出库单的创建、查询、确认等业务逻辑
 * 
 * @author 仓储系统
 * @since 1.0.0
 */
@Slf4j
@Service
public class OutboundOrderService {

    private final OutboundOrderRepository orderRepository;
    private final OutboundOrderDetailRepository detailRepository;
    private final InventoryRepository inventoryRepository;
    private final MaterialRepository materialRepository;
    private final WarehouseRepository warehouseRepository;

    public OutboundOrderService(OutboundOrderRepository orderRepository,
                                 OutboundOrderDetailRepository detailRepository,
                                 InventoryRepository inventoryRepository,
                                 MaterialRepository materialRepository,
                                 WarehouseRepository warehouseRepository) {
        this.orderRepository = orderRepository;
        this.detailRepository = detailRepository;
        this.inventoryRepository = inventoryRepository;
        this.materialRepository = materialRepository;
        this.warehouseRepository = warehouseRepository;
    }

    /**
     * 生成出库单号
     * 格式：CK + 年月日 + 4位序号
     * 
     * @return 出库单号
     */
    private String generateOrderNo() {
        String prefix = SystemConstants.OUTBOUND_ORDER_PREFIX + LocalDate.now().format(SystemConstants.DATE_FORMATTER);
        long count = orderRepository.countByOrderNoPrefix(prefix);
        return String.format("%s%04d", prefix, count + 1);
    }

    /**
     * 创建出库单
     * 
     * @param dto 出库单信息
     * @return 创建后的出库单信息
     */
    @Transactional(rollbackFor = Exception.class)
    public OutboundOrderDTO createOutboundOrder(OutboundOrderDTO dto) {
        log.info("开始创建出库单，仓库ID：{}", dto.getWarehouseId());

        OutboundOrder order = new OutboundOrder();
        order.setOrderNo(generateOrderNo());
        order.setWarehouseId(dto.getWarehouseId());
        order.setOutboundDate(dto.getOutboundDate());
        order.setOperator(dto.getOperator());
        order.setRecipient(dto.getRecipient());
        order.setRemark(dto.getRemark());
        order.setStatus(OrderStatus.PENDING.getCode());
        order = orderRepository.save(order);

        Long orderId = order.getId();
        for (OutboundOrderDetailDTO detailDTO : dto.getDetails()) {
            OutboundOrderDetail detail = new OutboundOrderDetail();
            detail.setOrderId(orderId);
            detail.setMaterialId(detailDTO.getMaterialId());
            detail.setQuantity(detailDTO.getQuantity());
            detail.setRemark(detailDTO.getRemark());
            detailRepository.save(detail);
        }

        log.info("出库单创建成功，单号：{}，ID：{}", order.getOrderNo(), orderId);
        return findOutboundOrderById(orderId);
    }

    /**
     * 确认出库单
     * 确认后将扣减对应仓库的库存数量
     * 
     * @param id 出库单ID
     */
    @Transactional(rollbackFor = Exception.class)
    public void confirmOutboundOrder(Long id) {
        log.info("开始确认出库单，ID：{}", id);

        OutboundOrder order = orderRepository.findById(id)
                .orElseThrow(() -> BusinessException.of(ErrorCode.OUTBOUND_ORDER_NOT_FOUND));

        if (!OrderStatus.PENDING.getCode().equals(order.getStatus())) {
            log.warn("出库单状态不正确，ID：{}，当前状态：{}", id, order.getStatus());
            throw BusinessException.of(ErrorCode.OUTBOUND_ORDER_STATUS_ERROR, "出库单状态不正确，无法确认");
        }

        Long warehouseId = order.getWarehouseId();
        List<OutboundOrderDetail> details = detailRepository.findByOrderId(id);

        for (OutboundOrderDetail detail : details) {
            Long materialId = detail.getMaterialId();
            Inventory inventory = inventoryRepository
                    .findByWarehouseIdAndMaterialId(warehouseId, materialId)
                    .orElseThrow(() -> {
                        log.warn("库存不存在，仓库ID：{}，物资ID：{}", warehouseId, materialId);
                        return BusinessException.of(ErrorCode.INVENTORY_INSUFFICIENT, "库存不足");
                    });

            if (inventory.getQuantity().compareTo(detail.getQuantity()) < 0) {
                log.warn("库存不足，仓库ID：{}，物资ID：{}，当前库存：{}，需出库：{}",
                        warehouseId, materialId, inventory.getQuantity(), detail.getQuantity());
                throw BusinessException.of(ErrorCode.INVENTORY_INSUFFICIENT, "库存不足");
            }

            inventory.setQuantity(inventory.getQuantity().subtract(detail.getQuantity()));
            inventoryRepository.save(inventory);
            log.debug("更新库存，仓库ID：{}，物资ID：{}，数量：{}", warehouseId, materialId, inventory.getQuantity());
        }

        order.setStatus(OrderStatus.CONFIRMED.getCode());
        orderRepository.save(order);

        log.info("出库单确认成功，ID：{}", id);
    }

    /**
     * 查询所有出库单
     * 
     * @return 出库单列表
     */
    @Transactional(readOnly = true)
    public List<OutboundOrderDTO> findAllOutboundOrders() {
        return orderRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * 根据ID查询出库单
     * 
     * @param id 出库单ID
     * @return 出库单信息
     */
    @Transactional(readOnly = true)
    public OutboundOrderDTO findOutboundOrderById(Long id) {
        OutboundOrder order = orderRepository.findById(id)
                .orElseThrow(() -> BusinessException.of(ErrorCode.OUTBOUND_ORDER_NOT_FOUND));
        return convertToDTO(order);
    }

    /**
     * 将出库单实体转换为DTO
     * 
     * @param order 出库单实体
     * @return 出库单DTO
     */
    private OutboundOrderDTO convertToDTO(OutboundOrder order) {
        OutboundOrderDTO dto = new OutboundOrderDTO();
        dto.setId(order.getId());
        dto.setOrderNo(order.getOrderNo());
        dto.setWarehouseId(order.getWarehouseId());
        dto.setOutboundDate(order.getOutboundDate());
        dto.setOperator(order.getOperator());
        dto.setRecipient(order.getRecipient());
        dto.setRemark(order.getRemark());
        dto.setStatus(order.getStatus());

        warehouseRepository.findById(order.getWarehouseId())
                .ifPresent(warehouse -> dto.setWarehouseName(warehouse.getWarehouseName()));

        List<OutboundOrderDetailDTO> detailDTOs = detailRepository.findByOrderId(order.getId())
                .stream()
                .map(this::convertDetailToDTO)
                .collect(Collectors.toList());

        dto.setDetails(detailDTOs);
        return dto;
    }

    /**
     * 将出库单明细实体转换为DTO
     * 
     * @param detail 出库单明细实体
     * @return 出库单明细DTO
     */
    private OutboundOrderDetailDTO convertDetailToDTO(OutboundOrderDetail detail) {
        OutboundOrderDetailDTO detailDTO = new OutboundOrderDetailDTO();
        detailDTO.setId(detail.getId());
        detailDTO.setMaterialId(detail.getMaterialId());
        detailDTO.setQuantity(detail.getQuantity());
        detailDTO.setRemark(detail.getRemark());

        materialRepository.findById(detail.getMaterialId())
                .ifPresent(material -> {
                    detailDTO.setMaterialCode(material.getMaterialCode());
                    detailDTO.setMaterialName(material.getMaterialName());
                    detailDTO.setSpecification(material.getSpecification());
                });

        return detailDTO;
    }
}
