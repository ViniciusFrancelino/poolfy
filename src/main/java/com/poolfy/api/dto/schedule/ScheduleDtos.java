package com.poolfy.api.dto.schedule;

import com.poolfy.api.entity.enums.ScheduleType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

public class ScheduleDtos {

    @Getter
    @Setter
    public static class CreateOrUpdateScheduleRequest {
        private Long poolId;
        private Long userId;
        private ScheduleType scheduleType;
        private Integer frequencyDays;
        private LocalDate nextScheduledDate;
        private Boolean active;
        private String notes;
    }

    @Getter
    @Setter
    public static class ScheduleResponse {
        private Long id;
        private Long poolId;
        private Long userId;
        private ScheduleType scheduleType;
        private Integer frequencyDays;
        private LocalDate nextScheduledDate;
        private Boolean active;
        private String notes;
    }
}
