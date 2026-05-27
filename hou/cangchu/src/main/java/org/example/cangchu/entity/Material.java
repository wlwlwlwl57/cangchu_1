package org.example.cangchu.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 物资实体类
 * 用于记录物资基本信息
 * 
 * @author 仓储系统
 * @since 1.0.0
 */
@Data
@Entity
@Table(name = "material", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"materialName", "specification", "material"})
})
public class Material implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String materialCode;

    @Column(nullable = false, length = 100)
    private String materialName;

    @Column(length = 100)
    private String specification;

    @Column(length = 50)
    private String material;

    @Column(length = 100)
    private String supplier;

    @Column(length = 50)
    private String brand;

    @Column(nullable = false)
    private Long categoryId;

    @Column(length = 20)
    private String unit;

    @Column(precision = 18, scale = 2)
    private BigDecimal unitPrice;

    @Column(length = 200)
    private String remark;

    @Column(nullable = false)
    private Boolean enabled = true;

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
