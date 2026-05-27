package com.poolfy.api.service;

import com.poolfy.api.dto.inventory.InventoryDtos.InventoryMovementRequest;
import com.poolfy.api.dto.maintenance.MaintenanceDtos.CreateMaintenanceRequest;
import com.poolfy.api.dto.maintenance.MaintenanceDtos.MaintenanceProductRequest;
import com.poolfy.api.dto.maintenance.MaintenanceDtos.MaintenanceProductResponse;
import com.poolfy.api.dto.maintenance.MaintenanceDtos.MaintenanceResponse;
import com.poolfy.api.dto.maintenance.MaintenanceDtos.ProductCalculationResponse;
import com.poolfy.api.entity.*;
import com.poolfy.api.entity.enums.ActivityType;
import com.poolfy.api.entity.enums.MovementType;
import com.poolfy.api.repository.MaintenanceProductRepository;
import com.poolfy.api.repository.MaintenanceRepository;
import com.poolfy.api.repository.PoolRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MaintenanceService {

    private final MaintenanceRepository maintenanceRepository;
    private final MaintenanceProductRepository maintenanceProductRepository;
    private final PoolRepository poolRepository;
    private final PoolService poolService;
    private final ProductService productService;
    private final InventoryService inventoryService;
    private final ActivityService activityService;
    private final UserService userService;

    public List<MaintenanceResponse> listByUser(Long userId) {
        userService.findUser(userId);
        return maintenanceRepository.findByUserIdOrderByMaintenanceDateDesc(userId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public List<MaintenanceResponse> listByPool(Long poolId) {
        poolService.findPool(poolId);
        return maintenanceRepository.findByPoolIdOrderByMaintenanceDateDesc(poolId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public List<MaintenanceResponse> searchByDate(Long userId, LocalDate startDate, LocalDate endDate) {
        userService.findUser(userId);
        return maintenanceRepository.findByUserIdAndMaintenanceDateBetweenOrderByMaintenanceDateDesc(userId, startDate, endDate)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public MaintenanceResponse create(CreateMaintenanceRequest request) {
        Pool pool = poolService.findPool(request.getPoolId());
        userService.findUser(request.getUserId());

        Maintenance maintenance = new Maintenance();
        maintenance.setPoolId(request.getPoolId());
        maintenance.setUserId(request.getUserId());
        maintenance.setMaintenanceDate(request.getMaintenanceDate());
        maintenance.setMaintenanceType(request.getMaintenanceType());
        maintenance.setDescription(request.getDescription());
        maintenance.setNextMaintenanceDate(request.getNextMaintenanceDate());
        maintenance = maintenanceRepository.save(maintenance);

        pool.setLastMaintenanceDate(request.getMaintenanceDate());
        pool.setNextMaintenanceDate(request.getNextMaintenanceDate());
        poolRepository.save(pool);

        if (request.getProducts() != null) {
            for (MaintenanceProductRequest productRequest : request.getProducts()) {
                Product product = productService.findProduct(productRequest.getProductId());
                BigDecimal calculated = pool.getVolumeM3()
                        .multiply(product.getDosagePerM3())
                        .setScale(2, RoundingMode.HALF_UP);

                MaintenanceProduct maintenanceProduct = new MaintenanceProduct();
                maintenanceProduct.setMaintenanceId(maintenance.getId());
                maintenanceProduct.setProductId(product.getId());
                maintenanceProduct.setPoolVolumeM3(pool.getVolumeM3());
                maintenanceProduct.setDosagePerM3(product.getDosagePerM3());
                maintenanceProduct.setDosageUnit(product.getDosageUnit());
                maintenanceProduct.setCalculatedQuantity(calculated);
                maintenanceProduct.setAppliedQuantity(productRequest.getAppliedQuantity() != null
                        ? productRequest.getAppliedQuantity()
                        : calculated);
                maintenanceProduct.setNotes(productRequest.getNotes());
                maintenanceProductRepository.save(maintenanceProduct);

                InventoryMovementRequest movementRequest = new InventoryMovementRequest();
                movementRequest.setUserId(request.getUserId());
                movementRequest.setProductId(product.getId());
                movementRequest.setMovementType(MovementType.OUT);
                movementRequest.setQuantity(maintenanceProduct.getAppliedQuantity());
                movementRequest.setUnit(product.getDosageUnit());
                movementRequest.setReason("Uso em manutenção #" + maintenance.getId());
                movementRequest.setMaintenanceId(maintenance.getId());
                inventoryService.registerMovement(movementRequest);
            }
        }

        activityService.register(request.getUserId(), ActivityType.MAINTENANCE_CREATED, maintenance.getId(),
                "Manutenção registrada para piscina: " + pool.getName());

        return toResponse(maintenance);
    }

    public ProductCalculationResponse calculateProduct(Long poolId, Long productId) {
        Pool pool = poolService.findPool(poolId);
        Product product = productService.findProduct(productId);

        ProductCalculationResponse response = new ProductCalculationResponse();
        response.setPoolId(pool.getId());
        response.setPoolName(pool.getName());
        response.setPoolVolumeM3(pool.getVolumeM3());
        response.setProductId(product.getId());
        response.setProductName(product.getName());
        response.setDosagePerM3(product.getDosagePerM3());
        response.setDosageUnit(product.getDosageUnit());
        response.setCalculatedQuantity(pool.getVolumeM3().multiply(product.getDosagePerM3()).setScale(2, RoundingMode.HALF_UP));
        return response;
    }

    private MaintenanceResponse toResponse(Maintenance maintenance) {
        Pool pool = poolService.findPool(maintenance.getPoolId());

        MaintenanceResponse response = new MaintenanceResponse();
        response.setId(maintenance.getId());
        response.setPoolId(maintenance.getPoolId());
        response.setPoolName(pool.getName());
        response.setUserId(maintenance.getUserId());
        response.setMaintenanceDate(maintenance.getMaintenanceDate());
        response.setMaintenanceType(maintenance.getMaintenanceType());
        response.setDescription(maintenance.getDescription());
        response.setNextMaintenanceDate(maintenance.getNextMaintenanceDate());
        response.setProducts(
                maintenanceProductRepository.findByMaintenanceId(maintenance.getId()).stream()
                        .map(this::toMaintenanceProductResponse)
                        .toList()
        );
        return response;
    }

    private MaintenanceProductResponse toMaintenanceProductResponse(MaintenanceProduct item) {
        Product product = productService.findProduct(item.getProductId());

        MaintenanceProductResponse response = new MaintenanceProductResponse();
        response.setProductId(product.getId());
        response.setProductName(product.getName());
        response.setPoolVolumeM3(item.getPoolVolumeM3());
        response.setDosagePerM3(item.getDosagePerM3());
        response.setDosageUnit(item.getDosageUnit());
        response.setCalculatedQuantity(item.getCalculatedQuantity());
        response.setAppliedQuantity(item.getAppliedQuantity());
        response.setNotes(item.getNotes());
        return response;
    }
}
