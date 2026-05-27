package com.poolfy.api.service;

import com.poolfy.api.dto.dashboard.DashboardDtos.DashboardResponse;
import com.poolfy.api.dto.dashboard.DashboardDtos.DashboardSummaryResponse;
import com.poolfy.api.dto.dashboard.DashboardDtos.QuickAccessResponse;
import com.poolfy.api.repository.AlertRepository;
import com.poolfy.api.repository.MaintenanceRepository;
import com.poolfy.api.repository.PoolRepository;
import com.poolfy.api.repository.RecentActivityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final PoolRepository poolRepository;
    private final MaintenanceRepository maintenanceRepository;
    private final RecentActivityRepository recentActivityRepository;
    private final AlertService alertService;
    private final UserService userService;

    public DashboardResponse getDashboard(Long userId) {
        userService.findUser(userId);

        DashboardSummaryResponse summary = new DashboardSummaryResponse();
        summary.setTotalPools(poolRepository.findByUserIdOrderByCreatedAtDesc(userId).size());
        summary.setPendingMaintenances(
                poolRepository.findByUserIdOrderByCreatedAtDesc(userId).stream()
                        .filter(pool -> pool.getNextMaintenanceDate() != null && pool.getNextMaintenanceDate().isBefore(LocalDate.now()))
                        .count()
        );
        summary.setUpcomingMaintenances(
                poolRepository.findByUserIdOrderByCreatedAtDesc(userId).stream()
                        .filter(pool -> pool.getNextMaintenanceDate() != null
                                && !pool.getNextMaintenanceDate().isBefore(LocalDate.now())
                                && !pool.getNextMaintenanceDate().isAfter(LocalDate.now().plusDays(7)))
                        .count()
        );
        summary.setUnreadAlerts(alertService.countUnread(userId));

        DashboardResponse response = new DashboardResponse();
        response.setSummary(summary);
        response.setRecentActivities(recentActivityRepository.findTop10ByUserIdOrderByCreatedAtDesc(userId));
        response.setAlerts(alertService.listByUser(userId));
        response.setQuickAccess(List.of(
                quick("Cadastrar piscina", "POST", "/api/pools"),
                quick("Registrar manutenção", "POST", "/api/maintenances")
        ));
        return response;
    }

    private QuickAccessResponse quick(String name, String method, String endpoint) {
        QuickAccessResponse item = new QuickAccessResponse();
        item.setName(name);
        item.setMethod(method);
        item.setEndpoint(endpoint);
        return item;
    }
}
