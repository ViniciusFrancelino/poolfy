package com.poolfy.api.controller;

import com.poolfy.api.dto.inventory.InventoryDtos.InventoryMovementRequest;
import com.poolfy.api.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    @GetMapping
    public Object list(@RequestParam Long userId) {
        return inventoryService.listByUser(userId);
    }

    @PostMapping("/movements")
    public Object registerMovement(@RequestBody InventoryMovementRequest request) {
        return inventoryService.registerMovement(request);
    }
}
