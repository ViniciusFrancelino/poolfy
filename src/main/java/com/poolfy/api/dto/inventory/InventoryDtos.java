package com.poolfy.api.dto.inventory;

import com.poolfy.api.entity.enums.DosageUnit;
import com.poolfy.api.entity.enums.MovementType;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class InventoryDtos {

    @Getter
    @Setter
    public static class InventoryResponse {
        private Long inventoryId;
        private Long productId;
        private String productName;
        private BigDecimal currentQuantity;
        private DosageUnit unit;
        private BigDecimal lowStockThreshold;
        private Boolean lowStock;
        private LocalDateTime updatedAt;
    }

    @Getter
    @Setter
    public static class InventoryMovementRequest {
        private Long userId;
        private Long productId;
        private MovementType movementType;
        private BigDecimal quantity;
        private DosageUnit unit;
        private String reason;
        private Long maintenanceId;
    }
}
