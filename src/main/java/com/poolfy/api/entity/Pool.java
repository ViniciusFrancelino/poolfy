package com.poolfy.api.entity;

import com.poolfy.api.entity.enums.PoolShape;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "pools")
@Getter
@Setter
public class Pool {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="user_id", nullable = false)
    private Long userId;

    @Column(nullable = false)
    private String name;

    @Column(name = "width_m")
    private BigDecimal widthM;

    @Column(name = "length_m")
    private BigDecimal lengthM;

    @Column(name = "depth_m")
    private BigDecimal depthM;

    @Column(name = "volume_m3", nullable = false)
    private BigDecimal volumeM3;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PoolShape shape;

    private String notes;

    @Column(name = "last_maintenance_date")
    private LocalDate lastMaintenanceDate;

    @Column(name = "next_maintenance_date")
    private LocalDate nextMaintenanceDate;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", insertable = false, updatable = false)
    private LocalDateTime updatedAt;
}
