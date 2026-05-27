package com.poolfy.api.controller;

import com.poolfy.api.dto.schedule.ScheduleDtos.CreateOrUpdateScheduleRequest;
import com.poolfy.api.service.MaintenanceScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/schedules")
@RequiredArgsConstructor
public class MaintenanceScheduleController {

    private final MaintenanceScheduleService maintenanceScheduleService;

    @GetMapping("/pool/{poolId}")
    public Object listByPool(@PathVariable Long poolId) {
        return maintenanceScheduleService.listByPool(poolId);
    }

    @PostMapping
    public Object create(@RequestBody CreateOrUpdateScheduleRequest request) {
        return maintenanceScheduleService.create(request);
    }

    @PutMapping("/{scheduleId}")
    public Object update(@PathVariable Long scheduleId, @RequestBody CreateOrUpdateScheduleRequest request) {
        return maintenanceScheduleService.update(scheduleId, request);
    }

    @PatchMapping("/{scheduleId}/deactivate")
    public Object deactivate(@PathVariable Long scheduleId) {
        return maintenanceScheduleService.deactivate(scheduleId);
    }
}
