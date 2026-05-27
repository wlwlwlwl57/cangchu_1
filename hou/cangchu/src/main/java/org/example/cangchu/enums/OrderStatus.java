package org.example.cangchu.enums;

import lombok.Getter;

/**
 * 订单状态枚举
 * 用于统一管理入库单和出库单的状态
 * 
 * @author仓储系统
 * @since 1.0.0
 */
@Getter
public enum OrderStatus {

    PENDING("PENDING", "待确认"),
    CONFIRMED("CONFIRMED", "已确认"),
    CANCELLED("CANCELLED", "已取消");

    private final String code;
    private final String description;

    OrderStatus(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public static OrderStatus fromCode(String code) {
        if (code == null) {
            return null;
        }
        for (OrderStatus status : values()) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }
        return null;
    }

    public static boolean isValidCode(String code) {
        return fromCode(code) != null;
    }
}
