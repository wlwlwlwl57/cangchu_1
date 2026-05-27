package org.example.cangchu.repository;

import org.example.cangchu.entity.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 仓库数据访问接口
 * 提供仓库数据的增删改查操作
 * 
 * @author 仓储系统
 * @since 1.0.0
 */
@Repository
public interface WarehouseRepository extends JpaRepository<Warehouse, Long> {

    /**
     * 检查仓库编码是否存在
     * 
     * @param warehouseCode 仓库编码
     * @return 是否存在
     */
    boolean existsByWarehouseCode(String warehouseCode);
}
