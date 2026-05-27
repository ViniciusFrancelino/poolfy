package com.poolfy.api.dto.pool;

import com.poolfy.api.entity.enums.PoolShape;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class PoolDtos {

    @Getter
    @Setter
    public static class CreateOrUpdatePoolRequest {
        private Long userId;
        private String name;
        private BigDecimal widthM;
        private BigDecimal lengthM;
        private BigDecimal depthM;
        private PoolShape shape;
        private String notes;
        private LocalDate nextMaintenanceDate;
    }

    @Getter
    @Setter
    public static class PoolResponse {
        private Long id;
        private Long userId;
        private String name;
        private BigDecimal widthM;
        private BigDecimal lengthM;
        private BigDecimal depthM;
        private BigDecimal volumeM3;
        private PoolShape shape;
        private String notes;
        private LocalDate lastMaintenanceDate;
        private LocalDate nextMaintenanceDate;
    }

    @Getter
    @Setter
    public static class PoolDetailsResponse {
        private PoolResponse pool;
        private List<?> maintenances;
        private List<?> schedules;
        private List<?> usedProducts;
    }
}
