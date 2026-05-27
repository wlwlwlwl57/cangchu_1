package org.example.cangchu.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.example.cangchu.dto.OutboundOrderDTO;
import org.example.cangchu.dto.Result;
import org.example.cangchu.service.OutboundOrderService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 出库单控制器
 * 提供出库单的创建、查询、确认等接口
 * 
 * @author 仓储系统
 * @since 1.0.0
 */
@Slf4j
@RestController
@RequestMapping("/api/outbound")
@CrossOrigin
public class OutboundOrderController {

    private final OutboundOrderService outboundOrderService;

    public OutboundOrderController(OutboundOrderService outboundOrderService) {
        this.outboundOrderService = outboundOrderService;
    }

    /**
     * 创建出库单
     * 
     * @param dto 出库单信息
     * @return 创建后的出库单信息
     */
    @PostMapping
    public Result<OutboundOrderDTO> createOutboundOrder(@Valid @RequestBody OutboundOrderDTO dto) {
        log.info("创建出库单，仓库ID：{}", dto.getWarehouseId());
        OutboundOrderDTO result = outboundOrderService.createOutboundOrder(dto);
        log.info("出库单创建成功，单号：{}", result.getOrderNo());
        return Result.success(result);
    }

    /**
     * 确认出库单
     * 确认后将扣减库存数量
     * 
     * @param id 出库单ID
     * @return 操作结果
     */
    @PostMapping("/{id}/confirm")
    public Result<Void> confirmOutboundOrder(@PathVariable Long id) {
        log.info("确认出库单，ID：{}", id);
        outboundOrderService.confirmOutboundOrder(id);
        log.info("出库单确认成功，ID：{}", id);
        return Result.success();
    }

    /**
     * 查询所有出库单
     * 
     * @return 出库单列表
     */
    @GetMapping
    public Result<List<OutboundOrderDTO>> findAllOutboundOrders() {
        return Result.success(outboundOrderService.findAllOutboundOrders());
    }

    /**
     * 根据ID查询出库单
     * 
     * @param id 出库单ID
     * @return 出库单信息
     */
    @GetMapping("/{id}")
    public Result<OutboundOrderDTO> findOutboundOrderById(@PathVariable Long id) {
        return Result.success(outboundOrderService.findOutboundOrderById(id));
    }
}
