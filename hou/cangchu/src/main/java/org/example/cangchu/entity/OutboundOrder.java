package org.example.cangchu.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.example.cangchu.enums.OrderStatus;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 出库单实体类
 * 用于记录物资出库信息
 * 
 * @author 仓储系统
 * @since 1.0.0
 */
@Data
@Entity
@Table(name = "outbound_order")
public class OutboundOrder implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String orderNo;

    @Column(nullable = false)
    private Long warehouseId;

    private LocalDateTime outboundDate;

    @Column(length = 50)
    private String operator;

    @Column(length = 100)
    private String recipient;

    @Column(length = 200)
    private String remark;

    @Column(nullable = false, length = 20)
    private String status = OrderStatus.PENDING.getCode();

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    @PrePersist
    protected void onCreate() {
        createTime = LocalDateTime.now();
        updateTime = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updateTime = LocalDateTime.now();
    }
}
