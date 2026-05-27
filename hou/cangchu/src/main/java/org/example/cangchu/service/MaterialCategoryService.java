package org.example.cangchu.service;

import lombok.extern.slf4j.Slf4j;
import org.example.cangchu.entity.MaterialCategory;
import org.example.cangchu.enums.ErrorCode;
import org.example.cangchu.exception.BusinessException;
import org.example.cangchu.repository.MaterialCategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 物资分类服务类
 * 提供物资分类的增删改查业务逻辑
 * 
 * @author 仓储系统
 * @since 1.0.0
 */
@Slf4j
@Service
public class MaterialCategoryService {

    private final MaterialCategoryRepository categoryRepository;

    public MaterialCategoryService(MaterialCategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    /**
     * 创建物资分类
     * 
     * @param category 分类信息
     * @return 创建后的分类信息
     */
    @Transactional(rollbackFor = Exception.class)
    public MaterialCategory createCategory(MaterialCategory category) {
        log.info("开始创建物资分类，编码：{}", category.getCategoryCode());

        if (categoryRepository.existsByCategoryCode(category.getCategoryCode())) {
            log.warn("分类编码已存在：{}", category.getCategoryCode());
            throw BusinessException.of(ErrorCode.CATEGORY_CODE_EXISTS);
        }

        MaterialCategory result = categoryRepository.save(category);
        log.info("物资分类创建成功，ID：{}", result.getId());
        return result;
    }

    /**
     * 查询所有物资分类
     * 
     * @return 分类列表
     */
    @Transactional(readOnly = true)
    public List<MaterialCategory> findAllCategories() {
        return categoryRepository.findAll();
    }

    /**
     * 根据ID查询物资分类
     * 
     * @param id 分类ID
     * @return 分类信息
     */
    @Transactional(readOnly = true)
    public MaterialCategory findCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> BusinessException.of(ErrorCode.CATEGORY_NOT_FOUND));
    }

    /**
     * 更新物资分类信息
     * 
     * @param id 分类ID
     * @param category 分类信息
     * @return 更新后的分类信息
     */
    @Transactional(rollbackFor = Exception.class)
    public MaterialCategory updateCategory(Long id, MaterialCategory category) {
        log.info("开始更新物资分类，ID：{}", id);

        MaterialCategory existing = findCategoryById(id);
        existing.setCategoryName(category.getCategoryName());
        existing.setDescription(category.getDescription());
        existing.setParentId(category.getParentId());

        MaterialCategory result = categoryRepository.save(existing);
        log.info("物资分类更新成功，ID：{}", id);
        return result;
    }
}
