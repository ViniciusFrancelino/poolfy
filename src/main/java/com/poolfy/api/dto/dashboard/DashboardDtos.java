package com.poolfy.api.dto.dashboard;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

public class DashboardDtos {

    @Getter
    @Setter
    public static class DashboardResponse {
        private DashboardSummaryResponse summary;
        private List<?> recentActivities;
        private List<?> alerts;
        private List<QuickAccessResponse> quickAccess;
    }

    @Getter
    @Setter
    public static class DashboardSummaryResponse {
        private long totalPools;
        private long pendingMaintenances;
        private long upcomingMaintenances;
        private long unreadAlerts;
    }

    @Getter
    @Setter
    public static class QuickAccessResponse {
        private String name;
        private String method;
        private String endpoint;
    }
}
