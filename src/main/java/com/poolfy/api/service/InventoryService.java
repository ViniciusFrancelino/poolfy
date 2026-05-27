package com.poolfy.api.service;

import com.poolfy.api.dto.inventory.InventoryDtos.InventoryMovementRequest;
import com.poolfy.api.dto.inventory.InventoryDtos.InventoryResponse;
import com.poolfy.api.entity.Inventory;
import com.poolfy.api.entity.InventoryMovement;
import com.poolfy.api.entity.Product;
import com.poolfy.api.entity.enums.ActivityType;
import com.poolfy.api.entity.enums.MovementType;
import com.poolfy.api.exception.BusinessException;
import com.poolfy.api.exception.NotFoundException;
import com.poolfy.api.repository.InventoryMovementRepository;
import com.poolfy.api.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final InventoryRepository inventoryRepository;
    private final InventoryMovementRepository inventoryMovementRepository;
    private final ProductService productService;
    private final AlertService alertService;
    private final ActivityService activityService;
    private final UserService userService;

    public List<InventoryResponse> listByUser(Long userId) {
        userService.findUser(userId);

        return inventoryRepository.findByUserIdOrderByUpdatedAtDesc(userId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public InventoryResponse registerMovement(InventoryMovementRequest request) {
        userService.findUser(request.getUserId());
        Product product = productService.findProduct(request.getProductId());

        Inventory inventory = inventoryRepository.findByUserIdAndProductId(request.getUserId(), request.getProductId())
                .orElseGet(() -> {
                    Inventory newInventory = new Inventory();
                    newInventory.setUserId(request.getUserId());
                    newInventory.setProductId(request.getProductId());
                    newInventory.setCurrentQuantity(BigDecimal.ZERO);
                    newInventory.setUnit(request.getUnit());
                    return inventoryRepository.save(newInventory);
                });

        BigDecimal current = inventory.getCurrentQuantity();
        BigDecimal newQuantity;

        if (request.getMovementType() == MovementType.IN) {
            newQuantity = current.add(request.getQuantity());
        } else {
            newQuantity = current.subtract(request.getQuantity());
            if (newQuantity.compareTo(BigDecimal.ZERO) < 0) {
                throw new BusinessException("Quantidade insuficiente em estoque.");
            }
        }

        inventory.setCurrentQuantity(newQuantity);
        inventory.setUnit(request.getUnit());
        inventory = inventoryRepository.save(inventory);

        InventoryMovement movement = new InventoryMovement();
        movement.setInventoryId(inventory.getId());
        movement.setProductId(request.getProductId());
        movement.setUserId(request.getUserId());
        movement.setMovementType(request.getMovementType());
        movement.setQuantity(request.getQuantity());
        movement.setUnit(request.getUnit());
        movement.setReason(request.getReason());
        movement.setMaintenanceId(request.getMaintenanceId());
        inventoryMovementRepository.save(movement);

        if (Boolean.TRUE.equals(product.getLowStockAlertEnabled())
                && product.getLowStockThreshold() != null
                && inventory.getCurrentQuantity().compareTo(product.getLowStockThreshold()) <= 0) {
            alertService.createLowStockAlert(request.getUserId(), product.getId(),
                    "Estoque baixo para o produto: " + product.getName());
        }

        activityService.register(request.getUserId(),
                request.getMovementType() == MovementType.IN ? ActivityType.STOCK_IN : ActivityType.STOCK_OUT,
                inventory.getId(),
                "Movimento de estoque " + request.getMovementType() + " para produto: " + product.getName());

        return toResponse(inventory);
    }

    public Inventory getByUserAndProduct(Long userId, Long productId) {
        return inventoryRepository.findByUserIdAndProductId(userId, productId)
                .orElseThrow(() -> new NotFoundException("Estoque não encontrado para o produto informado."));
    }

    private InventoryResponse toResponse(Inventory inventory) {
        Product product = productService.findProduct(inventory.getProductId());

        InventoryResponse response = new InventoryResponse();
        response.setInventoryId(inventory.getId());
        response.setProductId(product.getId());
        response.setProductName(product.getName());
        response.setCurrentQuantity(inventory.getCurrentQuantity());
        response.setUnit(inventory.getUnit());
        response.setLowStockThreshold(product.getLowStockThreshold());
        response.setLowStock(product.getLowStockThreshold() != null
                && inventory.getCurrentQuantity().compareTo(product.getLowStockThreshold()) <= 0);
        response.setUpdatedAt(inventory.getUpdatedAt());
        return response;
    }
}
