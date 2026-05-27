package org.example.cangchu.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.example.cangchu.dto.Result;
import org.example.cangchu.entity.MaterialCategory;
import org.example.cangchu.service.MaterialCategoryService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 物资分类控制器
 * 提供物资分类的增删改查接口
 * 
 * @author 仓储系统
 * @since 1.0.0
 */
@Slf4j
@RestController
@RequestMapping("/api/category")
@CrossOrigin
public class MaterialCategoryController {

    private final MaterialCategoryService materialCategoryService;

    public MaterialCategoryController(MaterialCategoryService materialCategoryService) {
        this.materialCategoryService = materialCategoryService;
    }

    /**
     * 创建物资分类
     * 
     * @param category 分类信息
     * @return 创建后的分类信息
     */
    @PostMapping
    public Result<MaterialCategory> createCategory(@Valid @RequestBody MaterialCategory category) {
        log.info("创建物资分类，编码：{}", category.getCategoryCode());
        MaterialCategory result = materialCategoryService.createCategory(category);
        log.info("物资分类创建成功，ID：{}", result.getId());
        return Result.success(result);
    }

    /**
     * 查询所有物资分类
     * 
     * @return 分类列表
     */
    @GetMapping
    public Result<List<MaterialCategory>> findAllCategories() {
        return Result.success(materialCategoryService.findAllCategories());
    }

    /**
     * 根据ID查询物资分类
     * 
     * @param id 分类ID
     * @return 分类信息
     */
    @GetMapping("/{id}")
    public Result<MaterialCategory> findCategoryById(@PathVariable Long id) {
        return Result.success(materialCategoryService.findCategoryById(id));
    }

    /**
     * 更新物资分类信息
     * 
     * @param id 分类ID
     * @param category 分类信息
     * @return 更新后的分类信息
     */
    @PutMapping("/{id}")
    public Result<MaterialCategory> updateCategory(@PathVariable Long id, @Valid @RequestBody MaterialCategory category) {
        log.info("更新物资分类，ID：{}", id);
        MaterialCategory result = materialCategoryService.updateCategory(id, category);
        log.info("物资分类更新成功，ID：{}", id);
        return Result.success(result);
    }
}
