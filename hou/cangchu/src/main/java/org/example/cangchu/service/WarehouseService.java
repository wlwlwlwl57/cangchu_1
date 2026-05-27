package org.example.cangchu.service;

import lombok.extern.slf4j.Slf4j;
import org.example.cangchu.entity.Warehouse;
import org.example.cangchu.enums.ErrorCode;
import org.example.cangchu.exception.BusinessException;
import org.example.cangchu.repository.WarehouseRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 仓库服务类
 * 提供仓库的增删改查业务逻辑
 * 
 * @author 仓储系统
 * @since 1.0.0
 */
@Slf4j
@Service
public class WarehouseService {

    private final WarehouseRepository warehouseRepository;

    public WarehouseService(WarehouseRepository warehouseRepository) {
        this.warehouseRepository = warehouseRepository;
    }

    /**
     * 创建仓库
     * 
     * @param warehouse 仓库信息
     * @return 创建后的仓库信息
     */
    @Transactional(rollbackFor = Exception.class)
    public Warehouse createWarehouse(Warehouse warehouse) {
        log.info("开始创建仓库，编码：{}", warehouse.getWarehouseCode());

        if (warehouseRepository.existsByWarehouseCode(warehouse.getWarehouseCode())) {
            log.warn("仓库编码已存在：{}", warehouse.getWarehouseCode());
            throw BusinessException.of(ErrorCode.WAREHOUSE_CODE_EXISTS);
        }

        Warehouse result = warehouseRepository.save(warehouse);
        log.info("仓库创建成功，ID：{}", result.getId());
        return result;
    }

    /**
     * 查询所有仓库
     * 
     * @return 仓库列表
     */
    @Transactional(readOnly = true)
    public List<Warehouse> findAllWarehouses() {
        return warehouseRepository.findAll();
    }

    /**
     * 根据ID查询仓库
     * 
     * @param id 仓库ID
     * @return 仓库信息
     */
    @Transactional(readOnly = true)
    public Warehouse findWarehouseById(Long id) {
        return warehouseRepository.findById(id)
                .orElseThrow(() -> BusinessException.of(ErrorCode.WAREHOUSE_NOT_FOUND));
    }

    /**
     * 更新仓库信息
     * 
     * @param id 仓库ID
     * @param warehouse 仓库信息
     * @return 更新后的仓库信息
     */
    @Transactional(rollbackFor = Exception.class)
    public Warehouse updateWarehouse(Long id, Warehouse warehouse) {
        log.info("开始更新仓库，ID：{}", id);

        Warehouse existing = findWarehouseById(id);
        existing.setWarehouseName(warehouse.getWarehouseName());
        existing.setAddress(warehouse.getAddress());
        existing.setManager(warehouse.getManager());
        existing.setPhone(warehouse.getPhone());

        Warehouse result = warehouseRepository.save(existing);
        log.info("仓库更新成功，ID：{}", id);
        return result;
    }
}
