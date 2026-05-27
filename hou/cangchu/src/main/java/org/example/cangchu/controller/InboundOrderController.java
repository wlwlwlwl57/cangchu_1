package org.example.cangchu.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.example.cangchu.dto.InboundOrderDTO;
import org.example.cangchu.dto.Result;
import org.example.cangchu.service.InboundOrderService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 入库单控制器
 * 提供入库单的创建、查询、确认等接口
 * 
 * @author 仓储系统
 * @since 1.0.0
 */
@Slf4j
@RestController
@RequestMapping("/api/inbound")
@CrossOrigin
public class InboundOrderController {

    private final InboundOrderService inboundOrderService;

    public InboundOrderController(InboundOrderService inboundOrderService) {
        this.inboundOrderService = inboundOrderService;
    }

    /**
     * 创建入库单
     * 
     * @param dto 入库单信息
     * @return 创建后的入库单信息
     */
    @PostMapping
    public Result<InboundOrderDTO> createInboundOrder(@Valid @RequestBody InboundOrderDTO dto) {
        log.info("创建入库单，仓库ID：{}", dto.getWarehouseId());
        InboundOrderDTO result = inboundOrderService.createInboundOrder(dto);
        log.info("入库单创建成功，单号：{}", result.getOrderNo());
        return Result.success(result);
    }

    /**
     * 确认入库单
     * 确认后将增加库存数量
     * 
     * @param id 入库单ID
     * @return 操作结果
     */
    @PostMapping("/{id}/confirm")
    public Result<Void> confirmInboundOrder(@PathVariable Long id) {
        log.info("确认入库单，ID：{}", id);
        inboundOrderService.confirmInboundOrder(id);
        log.info("入库单确认成功，ID：{}", id);
        return Result.success();
    }

    /**
     * 查询所有入库单
     * 
     * @return 入库单列表
     */
    @GetMapping
    public Result<List<InboundOrderDTO>> findAllInboundOrders() {
        return Result.success(inboundOrderService.findAllInboundOrders());
    }

    /**
     * 根据ID查询入库单
     * 
     * @param id 入库单ID
     * @return 入库单信息
     */
    @GetMapping("/{id}")
    public Result<InboundOrderDTO> findInboundOrderById(@PathVariable Long id) {
        return Result.success(inboundOrderService.findInboundOrderById(id));
    }
}
