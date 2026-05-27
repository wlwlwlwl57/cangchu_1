package org.example.cangchu.constant;

import java.time.format.DateTimeFormatter;

/**
 * 系统常量类
 * 统一管理魔法值，禁止硬编码
 * 
 * @author 仓储系统
 * @since 1.0.0
 */
public final class SystemConstants {

    private SystemConstants() {
    }

    public static final String INBOUND_ORDER_PREFIX = "RK";
    public static final String OUTBOUND_ORDER_PREFIX = "CK";

    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");
    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static final Integer DEFAULT_PAGE_SIZE = 20;
    public static final Integer MAX_PAGE_SIZE = 100;

    public static final String SUCCESS_MESSAGE = "操作成功";
    public static final String FAIL_MESSAGE = "操作失败";
}
