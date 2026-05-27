package com.poolfy.api.entity;

import com.poolfy.api.entity.enums.DosageUnit;
import com.poolfy.api.entity.enums.ProductType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "products")
@Getter
@Setter
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="user_id", nullable = false)
    private Long userId;

    @Column(name = "brand_id")
    private Long brandId;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "product_type", nullable = false)
    private ProductType productType;

    @Column(name = "dosage_per_m3", nullable = false)
    private BigDecimal dosagePerM3;

    @Enumerated(EnumType.STRING)
    @Column(name = "dosage_unit", nullable = false)
    private DosageUnit dosageUnit;

    private String description;

    @Column(name = "low_stock_alert_enabled", nullable = false)
    private Boolean lowStockAlertEnabled = true;

    @Column(name = "low_stock_threshold")
    private BigDecimal lowStockThreshold;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", insertable = false, updatable = false)
    private LocalDateTime updatedAt;
}
