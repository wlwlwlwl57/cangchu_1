package org.example.cangchu.enums;

import lombok.Getter;

/**
 * 错误码枚举
 * 统一管理系统错误码，便于前端处理和日志追踪
 * 
 * @author 仓储系统
 * @since 1.0.0
 */
@Getter
public enum ErrorCode {

    SUCCESS(200, "操作成功"),
    BAD_REQUEST(400, "请求参数错误"),
    NOT_FOUND(404, "资源不存在"),
    INTERNAL_ERROR(500, "服务器内部错误"),

    INBOUND_ORDER_NOT_FOUND(1001, "入库单不存在"),
    INBOUND_ORDER_STATUS_ERROR(1002, "入库单状态不正确"),
    INBOUND_ORDER_ALREADY_CONFIRMED(1003, "入库单已确认"),

    OUTBOUND_ORDER_NOT_FOUND(2001, "出库单不存在"),
    OUTBOUND_ORDER_STATUS_ERROR(2002, "出库单状态不正确"),
    OUTBOUND_ORDER_ALREADY_CONFIRMED(2003, "出库单已确认"),

    INVENTORY_NOT_FOUND(3001, "库存不存在"),
    INVENTORY_INSUFFICIENT(3002, "库存不足"),

    MATERIAL_NOT_FOUND(4001, "物资不存在"),
    MATERIAL_CODE_EXISTS(4002, "物资编码已存在"),
    MATERIAL_DUPLICATE(4003, "相同名称、规格、材质的物资已存在"),

    CATEGORY_NOT_FOUND(5001, "分类不存在"),
    CATEGORY_CODE_EXISTS(5002, "分类编码已存在"),

    WAREHOUSE_NOT_FOUND(6001, "仓库不存在"),
    WAREHOUSE_CODE_EXISTS(6002, "仓库编码已存在"),

    PARAM_VALID_ERROR(7001, "参数校验失败");

    private final Integer code;
    private final String message;

    ErrorCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
