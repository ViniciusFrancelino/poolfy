package com.poolfy.api.controller;

import com.poolfy.api.service.AlertService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/alerts")
@RequiredArgsConstructor
public class AlertController {

    private final AlertService alertService;

    @GetMapping
    public Object list(@RequestParam Long userId) {
        return alertService.listByUser(userId);
    }

    @PatchMapping("/{alertId}/read")
    public Object markAsRead(@PathVariable Long alertId) {
        return alertService.markAsRead(alertId);
    }
}
