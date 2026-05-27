package org.example.cangchu.repository;

import org.example.cangchu.entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 库存数据访问接口
 * 提供库存数据的增删改查操作
 * 
 * @author 仓储系统
 * @since 1.0.0
 */
@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {

    /**
     * 根据仓库ID和物资ID查询库存
     * 
     * @param warehouseId 仓库ID
     * @param materialId 物资ID
     * @return 库存信息
     */
    Optional<Inventory> findByWarehouseIdAndMaterialId(Long warehouseId, Long materialId);

    /**
     * 根据物资ID查询库存列表
     * 
     * @param materialId 物资ID
     * @return 库存列表
     */
    List<Inventory> findByMaterialId(Long materialId);
}
