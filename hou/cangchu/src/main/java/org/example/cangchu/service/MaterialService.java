package org.example.cangchu.service;

import lombok.extern.slf4j.Slf4j;
import org.example.cangchu.entity.Material;
import org.example.cangchu.enums.ErrorCode;
import org.example.cangchu.exception.BusinessException;
import org.example.cangchu.repository.MaterialRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 物资服务类
 * 提供物资的增删改查业务逻辑
 * 
 * @author 仓储系统
 * @since 1.0.0
 */
@Slf4j
@Service
public class MaterialService {

    private final MaterialRepository materialRepository;

    public MaterialService(MaterialRepository materialRepository) {
        this.materialRepository = materialRepository;
    }

    /**
     * 创建物资
     * 
     * @param material 物资信息
     * @return 创建后的物资信息
     */
    @Transactional(rollbackFor = Exception.class)
    public Material createMaterial(Material material) {
        log.info("开始创建物资，编码：{}", material.getMaterialCode());

        if (materialRepository.existsByMaterialCode(material.getMaterialCode())) {
            log.warn("物资编码已存在：{}", material.getMaterialCode());
            throw BusinessException.of(ErrorCode.MATERIAL_CODE_EXISTS);
        }

        if (materialRepository.existsByMaterialNameAndSpecificationAndMaterial(
                material.getMaterialName(), material.getSpecification(), material.getMaterial())) {
            log.warn("相同名称、规格、材质的物资已存在：{}", material.getMaterialName());
            throw BusinessException.of(ErrorCode.MATERIAL_DUPLICATE);
        }

        Material result = materialRepository.save(material);
        log.info("物资创建成功，ID：{}", result.getId());
        return result;
    }

    /**
     * 查询所有物资
     * 
     * @return 物资列表
     */
    @Transactional(readOnly = true)
    public List<Material> findAllMaterials() {
        return materialRepository.findAll();
    }

    /**
     * 根据ID查询物资
     * 
     * @param id 物资ID
     * @return 物资信息
     */
    @Transactional(readOnly = true)
    public Material findMaterialById(Long id) {
        return materialRepository.findById(id)
                .orElseThrow(() -> BusinessException.of(ErrorCode.MATERIAL_NOT_FOUND));
    }

    /**
     * 更新物资信息
     * 
     * @param id 物资ID
     * @param material 物资信息
     * @return 更新后的物资信息
     */
    @Transactional(rollbackFor = Exception.class)
    public Material updateMaterial(Long id, Material material) {
        log.info("开始更新物资，ID：{}", id);

        Material existing = findMaterialById(id);
        existing.setMaterialName(material.getMaterialName());
        existing.setSpecification(material.getSpecification());
        existing.setMaterial(material.getMaterial());
        existing.setSupplier(material.getSupplier());
        existing.setBrand(material.getBrand());
        existing.setCategoryId(material.getCategoryId());
        existing.setUnit(material.getUnit());
        existing.setUnitPrice(material.getUnitPrice());
        existing.setRemark(material.getRemark());

        Material result = materialRepository.save(existing);
        log.info("物资更新成功，ID：{}", id);
        return result;
    }

    /**
     * 删除物资
     * 
     * @param id 物资ID
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteMaterial(Long id) {
        log.info("开始删除物资，ID：{}", id);

        if (!materialRepository.existsById(id)) {
            throw BusinessException.of(ErrorCode.MATERIAL_NOT_FOUND);
        }

        materialRepository.deleteById(id);
        log.info("物资删除成功，ID：{}", id);
    }
}
