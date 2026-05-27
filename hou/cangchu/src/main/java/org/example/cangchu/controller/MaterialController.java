package org.example.cangchu.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.example.cangchu.dto.Result;
import org.example.cangchu.entity.Material;
import org.example.cangchu.service.MaterialService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 物资控制器
 * 提供物资的增删改查接口
 * 
 * @author 仓储系统
 * @since 1.0.0
 */
@Slf4j
@RestController
@RequestMapping("/api/material")
@CrossOrigin
public class MaterialController {

    private final MaterialService materialService;

    public MaterialController(MaterialService materialService) {
        this.materialService = materialService;
    }

    /**
     * 创建物资
     * 
     * @param material 物资信息
     * @return 创建后的物资信息
     */
    @PostMapping
    public Result<Material> createMaterial(@Valid @RequestBody Material material) {
        log.info("创建物资，编码：{}", material.getMaterialCode());
        Material result = materialService.createMaterial(material);
        log.info("物资创建成功，ID：{}", result.getId());
        return Result.success(result);
    }

    /**
     * 查询所有物资
     * 
     * @return 物资列表
     */
    @GetMapping
    public Result<List<Material>> findAllMaterials() {
        return Result.success(materialService.findAllMaterials());
    }

    /**
     * 根据ID查询物资
     * 
     * @param id 物资ID
     * @return 物资信息
     */
    @GetMapping("/{id}")
    public Result<Material> findMaterialById(@PathVariable Long id) {
        return Result.success(materialService.findMaterialById(id));
    }

    /**
     * 更新物资信息
     * 
     * @param id 物资ID
     * @param material 物资信息
     * @return 更新后的物资信息
     */
    @PutMapping("/{id}")
    public Result<Material> updateMaterial(@PathVariable Long id, @Valid @RequestBody Material material) {
        log.info("更新物资，ID：{}", id);
        Material result = materialService.updateMaterial(id, material);
        log.info("物资更新成功，ID：{}", id);
        return Result.success(result);
    }

    /**
     * 删除物资
     * 
     * @param id 物资ID
     * @return 操作结果
     */
    @DeleteMapping("/{id}")
    public Result<Void> deleteMaterial(@PathVariable Long id) {
        log.info("删除物资，ID：{}", id);
        materialService.deleteMaterial(id);
        log.info("物资删除成功，ID：{}", id);
        return Result.success();
    }
}
