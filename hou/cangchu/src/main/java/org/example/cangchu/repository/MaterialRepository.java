package org.example.cangchu.repository;

import org.example.cangchu.entity.Material;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 物资数据访问接口
 * 提供物资数据的增删改查操作
 * 
 * @author 仓储系统
 * @since 1.0.0
 */
@Repository
public interface MaterialRepository extends JpaRepository<Material, Long> {

    /**
     * 检查物资编码是否存在
     * 
     * @param materialCode 物资编码
     * @return 是否存在
     */
    boolean existsByMaterialCode(String materialCode);

    /**
     * 检查相同名称、规格、材质的物资是否存在
     * 
     * @param materialName 物资名称
     * @param specification 规格
     * @param material 材质
     * @return 是否存在
     */
    boolean existsByMaterialNameAndSpecificationAndMaterial(String materialName, String specification, String material);

    /**
     * 根据物资编码查询物资
     * 
     * @param materialCode 物资编码
     * @return 物资信息
     */
    Optional<Material> findByMaterialCode(String materialCode);

    /**
     * 根据关键字搜索物资
     * 支持按编码、名称、规格搜索
     * 
     * @param keyword 搜索关键字
     * @return 物资列表
     */
    @Query("SELECT m FROM Material m WHERE m.materialCode LIKE %:keyword% " +
           "OR m.materialName LIKE %:keyword% " +
           "OR m.specification LIKE %:keyword%")
    List<Material> searchByKeyword(@Param("keyword") String keyword);
}
