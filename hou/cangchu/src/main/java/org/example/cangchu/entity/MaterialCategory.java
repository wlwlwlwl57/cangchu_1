package org.example.cangchu.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 物资分类实体类
 * 用于管理物资分类信息
 * 
 * @author 仓储系统
 * @since 1.0.0
 */
@Data
@Entity
@Table(name = "material_category")
public class MaterialCategory implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String categoryCode;

    @Column(nullable = false, length = 100)
    private String categoryName;

    @Column(length = 200)
    private String description;

    private Long parentId;

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
