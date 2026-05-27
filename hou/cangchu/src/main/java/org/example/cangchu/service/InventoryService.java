package org.example.cangchu.service;

import lombok.extern.slf4j.Slf4j;
import org.example.cangchu.dto.InventoryDTO;
import org.example.cangchu.dto.MaterialCategoryInventoryDTO;
import org.example.cangchu.entity.Inventory;
import org.example.cangchu.entity.Material;
import org.example.cangchu.entity.MaterialCategory;
import org.example.cangchu.entity.Warehouse;
import org.example.cangchu.enums.ErrorCode;
import org.example.cangchu.exception.BusinessException;
import org.example.cangchu.repository.InventoryRepository;
import org.example.cangchu.repository.MaterialCategoryRepository;
import org.example.cangchu.repository.MaterialRepository;
import org.example.cangchu.repository.WarehouseRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 库存服务类
 * 提供库存查询、统计等业务逻辑
 * 
 * @author 仓储系统
 * @since 1.0.0
 */
@Slf4j
@Service
public class InventoryService {

    private final InventoryRepository inventoryRepository;
    private final MaterialRepository materialRepository;
    private final MaterialCategoryRepository categoryRepository;
    private final WarehouseRepository warehouseRepository;

    public InventoryService(InventoryRepository inventoryRepository,
                             MaterialRepository materialRepository,
                             MaterialCategoryRepository categoryRepository,
                             WarehouseRepository warehouseRepository) {
        this.inventoryRepository = inventoryRepository;
        this.materialRepository = materialRepository;
        this.categoryRepository = categoryRepository;
        this.warehouseRepository = warehouseRepository;
    }

    /**
     * 根据物资编码查询库存
     * 
     * @param materialCode 物资编码
     * @return 库存信息列表
     */
    @Transactional(readOnly = true)
    public List<InventoryDTO> findInventoryByMaterialCode(String materialCode) {
        log.info("根据物资编码查询库存，编码：{}", materialCode);

        Material material = materialRepository.findByMaterialCode(materialCode)
                .orElseThrow(() -> BusinessException.of(ErrorCode.MATERIAL_NOT_FOUND));

        return inventoryRepository.findByMaterialId(material.getId())
                .stream()
                .map(inventory -> convertToDTO(inventory, material))
                .collect(Collectors.toList());
    }

    /**
     * 根据关键字搜索库存
     * 支持按物资编码、名称、规格搜索
     * 
     * @param keyword 搜索关键字
     * @return 库存信息列表
     */
    @Transactional(readOnly = true)
    public List<InventoryDTO> searchInventoryByKeyword(String keyword) {
        log.info("根据关键字搜索库存，关键字：{}", keyword);

        List<Material> materials = materialRepository.searchByKeyword(keyword);

        List<InventoryDTO> result = new ArrayList<>();
        for (Material material : materials) {
            List<Inventory> inventories = inventoryRepository.findByMaterialId(material.getId());
            for (Inventory inventory : inventories) {
                result.add(convertToDTO(inventory, material));
            }
        }
        return result;
    }

    /**
     * 按分类统计库存
     * 
     * @return 分类库存统计列表
     */
    @Transactional(readOnly = true)
    public List<MaterialCategoryInventoryDTO> findInventoryGroupByCategory() {
        log.info("按分类统计库存");

        List<MaterialCategory> categories = categoryRepository.findAll();
        Map<Long, MaterialCategoryInventoryDTO> categoryMap = new HashMap<>();

        for (MaterialCategory category : categories) {
            MaterialCategoryInventoryDTO dto = new MaterialCategoryInventoryDTO();
            dto.setCategoryId(category.getId());
            dto.setCategoryCode(category.getCategoryCode());
            dto.setCategoryName(category.getCategoryName());
            dto.setTotalQuantity(BigDecimal.ZERO);
            categoryMap.put(category.getId(), dto);
        }

        List<Inventory> inventories = inventoryRepository.findAll();
        for (Inventory inventory : inventories) {
            materialRepository.findById(inventory.getMaterialId())
                    .ifPresent(material -> {
                        Long categoryId = material.getCategoryId();
                        if (categoryMap.containsKey(categoryId)) {
                            MaterialCategoryInventoryDTO dto = categoryMap.get(categoryId);
                            dto.setTotalQuantity(dto.getTotalQuantity().add(inventory.getQuantity()));
                        }
                    });
        }

        return new ArrayList<>(categoryMap.values());
    }

    /**
     * 将库存实体转换为DTO
     * 
     * @param inventory 库存实体
     * @param material 物资实体
     * @return 库存DTO
     */
    private InventoryDTO convertToDTO(Inventory inventory, Material material) {
        InventoryDTO dto = new InventoryDTO();
        dto.setMaterialId(material.getId());
        dto.setMaterialCode(material.getMaterialCode());
        dto.setMaterialName(material.getMaterialName());
        dto.setSpecification(material.getSpecification());
        dto.setMaterial(material.getMaterial());
        dto.setQuantity(inventory.getQuantity());
        dto.setUnit(material.getUnit());

        categoryRepository.findById(material.getCategoryId())
                .ifPresent(category -> dto.setCategoryName(category.getCategoryName()));

        warehouseRepository.findById(inventory.getWarehouseId())
                .ifPresent(warehouse -> dto.setWarehouseName(warehouse.getWarehouseName()));

        return dto;
    }
}
