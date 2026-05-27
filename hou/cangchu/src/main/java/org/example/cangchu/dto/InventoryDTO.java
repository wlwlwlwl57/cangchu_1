package org.example.cangchu.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 库存数据传输对象
 * 用于库存查询结果展示
 * 
 * @author 仓储系统
 * @since 1.0.0
 */
@Data
public class InventoryDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long materialId;

    private String materialCode;

    private String materialName;

    private String specification;

    private String material;

    private String categoryName;

    private String warehouseName;

    private BigDecimal quantity;

    private String unit;
}
