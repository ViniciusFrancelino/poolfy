package com.poolfy.api.controller;

import com.poolfy.api.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/{userId}")
    public Object getDashboard(@PathVariable Long userId) {
        return dashboardService.getDashboard(userId);
    }
}
