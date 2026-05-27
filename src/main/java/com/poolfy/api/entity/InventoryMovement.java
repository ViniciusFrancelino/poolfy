package com.poolfy.api.entity;

import com.poolfy.api.entity.enums.DosageUnit;
import com.poolfy.api.entity.enums.MovementType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "inventory_movements")
@Getter
@Setter
public class InventoryMovement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="inventory_id", nullable = false)
    private Long inventoryId;

    @Column(name="product_id", nullable = false)
    private Long productId;

    @Column(name="user_id", nullable = false)
    private Long userId;

    @Enumerated(EnumType.STRING)
    @Column(name = "movement_type", nullable = false)
    private MovementType movementType;

    @Column(nullable = false)
    private BigDecimal quantity;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DosageUnit unit;

    private String reason;

    @Column(name = "maintenance_id")
    private Long maintenanceId;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;
}
