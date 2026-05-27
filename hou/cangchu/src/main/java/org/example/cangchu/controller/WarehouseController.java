package org.example.cangchu.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.example.cangchu.dto.Result;
import org.example.cangchu.entity.Warehouse;
import org.example.cangchu.service.WarehouseService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 仓库控制器
 * 提供仓库的增删改查接口
 * 
 * @author 仓储系统
 * @since 1.0.0
 */
@Slf4j
@RestController
@RequestMapping("/api/warehouse")
@CrossOrigin
public class WarehouseController {

    private final WarehouseService warehouseService;

    public WarehouseController(WarehouseService warehouseService) {
        this.warehouseService = warehouseService;
    }

    /**
     * 创建仓库
     * 
     * @param warehouse 仓库信息
     * @return 创建后的仓库信息
     */
    @PostMapping
    public Result<Warehouse> createWarehouse(@Valid @RequestBody Warehouse warehouse) {
        log.info("创建仓库，编码：{}", warehouse.getWarehouseCode());
        Warehouse result = warehouseService.createWarehouse(warehouse);
        log.info("仓库创建成功，ID：{}", result.getId());
        return Result.success(result);
    }

    /**
     * 查询所有仓库
     * 
     * @return 仓库列表
     */
    @GetMapping
    public Result<List<Warehouse>> findAllWarehouses() {
        return Result.success(warehouseService.findAllWarehouses());
    }

    /**
     * 根据ID查询仓库
     * 
     * @param id 仓库ID
     * @return 仓库信息
     */
    @GetMapping("/{id}")
    public Result<Warehouse> findWarehouseById(@PathVariable Long id) {
        return Result.success(warehouseService.findWarehouseById(id));
    }

    /**
     * 更新仓库信息
     * 
     * @param id 仓库ID
     * @param warehouse 仓库信息
     * @return 更新后的仓库信息
     */
    @PutMapping("/{id}")
    public Result<Warehouse> updateWarehouse(@PathVariable Long id, @Valid @RequestBody Warehouse warehouse) {
        log.info("更新仓库，ID：{}", id);
        Warehouse result = warehouseService.updateWarehouse(id, warehouse);
        log.info("仓库更新成功，ID：{}", id);
        return Result.success(result);
    }
}
