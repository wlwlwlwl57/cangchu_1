package org.example.cangchu.repository;

import org.example.cangchu.entity.MaterialCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 物资分类数据访问接口
 * 提供物资分类数据的增删改查操作
 * 
 * @author 仓储系统
 * @since 1.0.0
 */
@Repository
public interface MaterialCategoryRepository extends JpaRepository<MaterialCategory, Long> {

    /**
     * 检查分类编码是否存在
     * 
     * @param categoryCode 分类编码
     * @return 是否存在
     */
    boolean existsByCategoryCode(String categoryCode);
}
