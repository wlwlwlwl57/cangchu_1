package org.example.cangchu.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.cangchu.dto.InventoryDTO;
import org.example.cangchu.dto.MaterialCategoryInventoryDTO;
import org.example.cangchu.dto.Result;
import org.example.cangchu.service.InventoryService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 库存控制器
 * 提供库存查询相关接口
 * 
 * @author 仓储系统
 * @since 1.0.0
 */
@Slf4j
@RestController
@RequestMapping("/api/inventory")
@CrossOrigin
public class InventoryController {

    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    /**
     * 根据物资编码查询库存
     * 
     * @param materialCode 物资编码
     * @return 库存信息列表
     */
    @GetMapping("/material/{materialCode}")
    public Result<List<InventoryDTO>> findInventoryByMaterialCode(@PathVariable String materialCode) {
        return Result.success(inventoryService.findInventoryByMaterialCode(materialCode));
    }

    /**
     * 根据关键字搜索库存
     * 支持按物资编码、名称、规格搜索
     * 
     * @param keyword 搜索关键字
     * @return 库存信息列表
     */
    @GetMapping("/search")
    public Result<List<InventoryDTO>> searchInventoryByKeyword(@RequestParam String keyword) {
        return Result.success(inventoryService.searchInventoryByKeyword(keyword));
    }

    /**
     * 按分类统计库存
     * 
     * @return 分类库存统计列表
     */
    @GetMapping("/category")
    public Result<List<MaterialCategoryInventoryDTO>> findInventoryGroupByCategory() {
        return Result.success(inventoryService.findInventoryGroupByCategory());
    }
}
