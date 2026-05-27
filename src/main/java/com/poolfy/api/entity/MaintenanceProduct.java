package com.poolfy.api.entity;

import com.poolfy.api.entity.enums.DosageUnit;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "maintenance_products")
@Getter
@Setter
public class MaintenanceProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="maintenance_id", nullable = false)
    private Long maintenanceId;

    @Column(name="product_id", nullable = false)
    private Long productId;

    @Column(name = "pool_volume_m3", nullable = false)
    private BigDecimal poolVolumeM3;

    @Column(name = "dosage_per_m3", nullable = false)
    private BigDecimal dosagePerM3;

    @Enumerated(EnumType.STRING)
    @Column(name = "dosage_unit", nullable = false)
    private DosageUnit dosageUnit;

    @Column(name = "calculated_quantity", nullable = false)
    private BigDecimal calculatedQuantity;

    @Column(name = "applied_quantity")
    private BigDecimal appliedQuantity;

    private String notes;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;
}
