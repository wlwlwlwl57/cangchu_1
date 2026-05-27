package org.example.cangchu.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 入库单数据传输对象
 * 用于入库单的新增、修改和查询操作
 * 
 * @author 仓储系统
 * @since 1.0.0
 */
@Data
public class InboundOrderDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String orderNo;

    @NotNull(message = "仓库ID不能为空")
    private Long warehouseId;

    private String warehouseName;

    private LocalDateTime inboundDate;

    private String operator;

    private String remark;

    private String status;

    @NotEmpty(message = "入库明细不能为空")
    private List<InboundOrderDetailDTO> details;
}
