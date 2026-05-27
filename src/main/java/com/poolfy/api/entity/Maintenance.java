package com.poolfy.api.entity;

import com.poolfy.api.entity.enums.MaintenanceType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "maintenances")
@Getter
@Setter
public class Maintenance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="pool_id", nullable = false)
    private Long poolId;

    @Column(name="user_id", nullable = false)
    private Long userId;

    @Column(name = "maintenance_date", nullable = false)
    private LocalDate maintenanceDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "maintenance_type", nullable = false)
    private MaintenanceType maintenanceType;

    private String description;

    @Column(name = "next_maintenance_date")
    private LocalDate nextMaintenanceDate;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", insertable = false, updatable = false)
    private LocalDateTime updatedAt;
}
