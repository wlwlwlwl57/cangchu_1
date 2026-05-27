package org.example.cangchu.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 入库单明细数据传输对象
 * 用于入库单明细的新增和修改操作
 * 
 * @author 仓储系统
 * @since 1.0.0
 */
@Data
public class InboundOrderDetailDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    @NotNull(message = "物资ID不能为空")
    private Long materialId;

    private String materialCode;

    private String materialName;

    private String specification;

    @NotNull(message = "数量不能为空")
    @Positive(message = "数量必须大于0")
    private BigDecimal quantity;

    @Positive(message = "单价必须大于0")
    private BigDecimal unitPrice;

    private String remark;
}
