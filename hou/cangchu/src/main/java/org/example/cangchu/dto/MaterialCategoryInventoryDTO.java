package org.example.cangchu.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 物资分类库存统计DTO
 * 用于按分类统计库存数量的查询结果展示
 * 
 * @author 仓储系统
 * @since 1.0.0
 */
@Data
public class MaterialCategoryInventoryDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long categoryId;

    private String categoryCode;

    private String categoryName;

    private BigDecimal totalQuantity;
}
