package com.poolfy.api.controller;

import com.poolfy.api.dto.maintenance.MaintenanceDtos.CreateMaintenanceRequest;
import com.poolfy.api.service.MaintenanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/maintenances")
@RequiredArgsConstructor
public class MaintenanceController {

    private final MaintenanceService maintenanceService;

    @GetMapping
    public Object list(@RequestParam Long userId) {
        return maintenanceService.listByUser(userId);
    }

    @GetMapping("/pool/{poolId}")
    public Object listByPool(@PathVariable Long poolId) {
        return maintenanceService.listByPool(poolId);
    }

    @GetMapping("/search")
    public Object searchByDate(@RequestParam Long userId,
                               @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                               @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return maintenanceService.searchByDate(userId, startDate, endDate);
    }

    @GetMapping("/calculate")
    public Object calculate(@RequestParam Long poolId, @RequestParam Long productId) {
        return maintenanceService.calculateProduct(poolId, productId);
    }

    @PostMapping
    public Object create(@RequestBody CreateMaintenanceRequest request) {
        return maintenanceService.create(request);
    }
}
