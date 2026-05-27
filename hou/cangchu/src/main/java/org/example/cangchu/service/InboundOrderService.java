package org.example.cangchu.service;

import lombok.extern.slf4j.Slf4j;
import org.example.cangchu.constant.SystemConstants;
import org.example.cangchu.dto.InboundOrderDTO;
import org.example.cangchu.dto.InboundOrderDetailDTO;
import org.example.cangchu.entity.InboundOrder;
import org.example.cangchu.entity.InboundOrderDetail;
import org.example.cangchu.entity.Inventory;
import org.example.cangchu.entity.Material;
import org.example.cangchu.entity.Warehouse;
import org.example.cangchu.enums.ErrorCode;
import org.example.cangchu.enums.OrderStatus;
import org.example.cangchu.exception.BusinessException;
import org.example.cangchu.repository.InboundOrderDetailRepository;
import org.example.cangchu.repository.InboundOrderRepository;
import org.example.cangchu.repository.InventoryRepository;
import org.example.cangchu.repository.MaterialRepository;
import org.example.cangchu.repository.WarehouseRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 入库单服务类
 * 提供入库单的创建、查询、确认等业务逻辑
 * 
 * @author 仓储系统
 * @since 1.0.0
 */
@Slf4j
@Service
public class InboundOrderService {

    private final InboundOrderRepository orderRepository;
    private final InboundOrderDetailRepository detailRepository;
    private final InventoryRepository inventoryRepository;
    private final MaterialRepository materialRepository;
    private final WarehouseRepository warehouseRepository;

    public InboundOrderService(InboundOrderRepository orderRepository,
                               InboundOrderDetailRepository detailRepository,
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
     * 生成入库单号
     * 格式：RK + 年月日 + 4位序号
     * 
     * @return 入库单号
     */
    private String generateOrderNo() {
        String prefix = SystemConstants.INBOUND_ORDER_PREFIX + LocalDate.now().format(SystemConstants.DATE_FORMATTER);
        long count = orderRepository.countByOrderNoPrefix(prefix);
        return String.format("%s%04d", prefix, count + 1);
    }

    /**
     * 创建入库单
     * 
     * @param dto 入库单信息
     * @return 创建后的入库单信息
     */
    @Transactional(rollbackFor = Exception.class)
    public InboundOrderDTO createInboundOrder(InboundOrderDTO dto) {
        log.info("开始创建入库单，仓库ID：{}", dto.getWarehouseId());

        InboundOrder order = new InboundOrder();
        order.setOrderNo(generateOrderNo());
        order.setWarehouseId(dto.getWarehouseId());
        order.setInboundDate(dto.getInboundDate());
        order.setOperator(dto.getOperator());
        order.setRemark(dto.getRemark());
        order.setStatus(OrderStatus.PENDING.getCode());
        order = orderRepository.save(order);

        Long orderId = order.getId();
        for (InboundOrderDetailDTO detailDTO : dto.getDetails()) {
            InboundOrderDetail detail = new InboundOrderDetail();
            detail.setOrderId(orderId);
            detail.setMaterialId(detailDTO.getMaterialId());
            detail.setQuantity(detailDTO.getQuantity());
            detail.setUnitPrice(detailDTO.getUnitPrice());
            detail.setRemark(detailDTO.getRemark());
            detailRepository.save(detail);
        }

        log.info("入库单创建成功，单号：{}，ID：{}", order.getOrderNo(), orderId);
        return findInboundOrderById(orderId);
    }

    /**
     * 确认入库单
     * 确认后将增加对应仓库的库存数量
     * 
     * @param id 入库单ID
     */
    @Transactional(rollbackFor = Exception.class)
    public void confirmInboundOrder(Long id) {
        log.info("开始确认入库单，ID：{}", id);

        InboundOrder order = orderRepository.findById(id)
                .orElseThrow(() -> BusinessException.of(ErrorCode.INBOUND_ORDER_NOT_FOUND));

        if (!OrderStatus.PENDING.getCode().equals(order.getStatus())) {
            log.warn("入库单状态不正确，ID：{}，当前状态：{}", id, order.getStatus());
            throw BusinessException.of(ErrorCode.INBOUND_ORDER_STATUS_ERROR, "入库单状态不正确，无法确认");
        }

        Long warehouseId = order.getWarehouseId();
        List<InboundOrderDetail> details = detailRepository.findByOrderId(id);

        for (InboundOrderDetail detail : details) {
            Long materialId = detail.getMaterialId();
            Inventory inventory = inventoryRepository
                    .findByWarehouseIdAndMaterialId(warehouseId, materialId)
                    .orElseGet(() -> {
                        Inventory newInventory = new Inventory();
                        newInventory.setWarehouseId(warehouseId);
                        newInventory.setMaterialId(materialId);
                        return newInventory;
                    });

            if (inventory.getId() == null) {
                inventory.setQuantity(detail.getQuantity());
            } else {
                inventory.setQuantity(inventory.getQuantity().add(detail.getQuantity()));
            }
            inventoryRepository.save(inventory);
            log.debug("更新库存，仓库ID：{}，物资ID：{}，数量：{}", warehouseId, materialId, inventory.getQuantity());
        }

        order.setStatus(OrderStatus.CONFIRMED.getCode());
        orderRepository.save(order);

        log.info("入库单确认成功，ID：{}", id);
    }

    /**
     * 查询所有入库单
     * 
     * @return 入库单列表
     */
    @Transactional(readOnly = true)
    public List<InboundOrderDTO> findAllInboundOrders() {
        return orderRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * 根据ID查询入库单
     * 
     * @param id 入库单ID
     * @return 入库单信息
     */
    @Transactional(readOnly = true)
    public InboundOrderDTO findInboundOrderById(Long id) {
        InboundOrder order = orderRepository.findById(id)
                .orElseThrow(() -> BusinessException.of(ErrorCode.INBOUND_ORDER_NOT_FOUND));
        return convertToDTO(order);
    }

    /**
     * 将入库单实体转换为DTO
     * 
     * @param order 入库单实体
     * @return 入库单DTO
     */
    private InboundOrderDTO convertToDTO(InboundOrder order) {
        InboundOrderDTO dto = new InboundOrderDTO();
        dto.setId(order.getId());
        dto.setOrderNo(order.getOrderNo());
        dto.setWarehouseId(order.getWarehouseId());
        dto.setInboundDate(order.getInboundDate());
        dto.setOperator(order.getOperator());
        dto.setRemark(order.getRemark());
        dto.setStatus(order.getStatus());

        warehouseRepository.findById(order.getWarehouseId())
                .ifPresent(warehouse -> dto.setWarehouseName(warehouse.getWarehouseName()));

        List<InboundOrderDetailDTO> detailDTOs = detailRepository.findByOrderId(order.getId())
                .stream()
                .map(this::convertDetailToDTO)
                .collect(Collectors.toList());

        dto.setDetails(detailDTOs);
        return dto;
    }

    /**
     * 将入库单明细实体转换为DTO
     * 
     * @param detail 入库单明细实体
     * @return 入库单明细DTO
     */
    private InboundOrderDetailDTO convertDetailToDTO(InboundOrderDetail detail) {
        InboundOrderDetailDTO detailDTO = new InboundOrderDetailDTO();
        detailDTO.setId(detail.getId());
        detailDTO.setMaterialId(detail.getMaterialId());
        detailDTO.setQuantity(detail.getQuantity());
        detailDTO.setUnitPrice(detail.getUnitPrice());
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
