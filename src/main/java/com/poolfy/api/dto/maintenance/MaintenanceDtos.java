package com.poolfy.api.dto.maintenance;

import com.poolfy.api.entity.enums.DosageUnit;
import com.poolfy.api.entity.enums.MaintenanceType;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class MaintenanceDtos {

    @Getter
    @Setter
    public static class MaintenanceProductRequest {
        private Long productId;
        private BigDecimal appliedQuantity;
        private String notes;
    }

    @Getter
    @Setter
    public static class CreateMaintenanceRequest {
        private Long userId;
        private Long poolId;
        private LocalDate maintenanceDate;
        private MaintenanceType maintenanceType;
        private String description;
        private LocalDate nextMaintenanceDate;
        private List<MaintenanceProductRequest> products;
    }

    @Getter
    @Setter
    public static class MaintenanceProductResponse {
        private Long productId;
        private String productName;
        private BigDecimal poolVolumeM3;
        private BigDecimal dosagePerM3;
        private DosageUnit dosageUnit;
        private BigDecimal calculatedQuantity;
        private BigDecimal appliedQuantity;
        private String notes;
    }

    @Getter
    @Setter
    public static class MaintenanceResponse {
        private Long id;
        private Long poolId;
        private String poolName;
        private Long userId;
        private LocalDate maintenanceDate;
        private MaintenanceType maintenanceType;
        private String description;
        private LocalDate nextMaintenanceDate;
        private List<MaintenanceProductResponse> products;
    }

    @Getter
    @Setter
    public static class ProductCalculationResponse {
        private Long poolId;
        private String poolName;
        private BigDecimal poolVolumeM3;
        private Long productId;
        private String productName;
        private BigDecimal dosagePerM3;
        private DosageUnit dosageUnit;
        private BigDecimal calculatedQuantity;
    }
}
