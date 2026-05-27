package org.example.cangchu.enums;

import lombok.Getter;

/**
 * 启用状态枚举
 * 用于统一管理仓库、物资、分类等实体的启用/禁用状态
 * 
 * @author 仓储系统
 * @since 1.0.0
 */
@Getter
public enum EnableStatus {

    ENABLED(true, "启用"),
    DISABLED(false, "禁用");

    private final Boolean code;
    private final String description;

    EnableStatus(Boolean code, String description) {
        this.code = code;
        this.description = description;
    }

    public static EnableStatus fromCode(Boolean code) {
        if (code == null) {
            return null;
        }
        return code ? ENABLED : DISABLED;
    }
}
