package com.poolfy.api.entity;

import com.poolfy.api.entity.enums.DosageUnit;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "inventory")
@Getter
@Setter
public class Inventory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="user_id", nullable = false)
    private Long userId;

    @Column(name="product_id", nullable = false)
    private Long productId;

    @Column(name = "current_quantity", nullable = false)
    private BigDecimal currentQuantity;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DosageUnit unit;

    @Column(name = "updated_at", insertable = false, updatable = false)
    private LocalDateTime updatedAt;
}
