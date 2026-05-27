package com.poolfy.api.dto.product;

import com.poolfy.api.entity.enums.DosageUnit;
import com.poolfy.api.entity.enums.ProductType;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

public class ProductDtos {

    @Getter
    @Setter
    public static class CreateOrUpdateProductRequest {
        private Long userId;
        private Long brandId;
        private String name;
        private ProductType productType;
        private BigDecimal dosagePerM3;
        private DosageUnit dosageUnit;
        private String description;
        private Boolean lowStockAlertEnabled;
        private BigDecimal lowStockThreshold;
    }

    @Getter
    @Setter
    public static class ProductResponse {
        private Long id;
        private Long userId;
        private Long brandId;
        private String brandName;
        private String name;
        private ProductType productType;
        private BigDecimal dosagePerM3;
        private DosageUnit dosageUnit;
        private String description;
        private Boolean lowStockAlertEnabled;
        private BigDecimal lowStockThreshold;
    }

    @Getter
    @Setter
    public static class BrandRequest {
        private Long userId;
        private String brandName;
    }

    @Getter
    @Setter
    public static class BrandResponse {
        private Long id;
        private Long userId;
        private String brandName;
    }
}
