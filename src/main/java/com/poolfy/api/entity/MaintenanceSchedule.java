package com.poolfy.api.entity;

import com.poolfy.api.entity.enums.ScheduleType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "maintenance_schedules")
@Getter
@Setter
public class MaintenanceSchedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="pool_id", nullable = false)
    private Long poolId;

    @Column(name="user_id", nullable = false)
    private Long userId;

    @Enumerated(EnumType.STRING)
    @Column(name = "schedule_type", nullable = false)
    private ScheduleType scheduleType;

    @Column(name = "frequency_days")
    private Integer frequencyDays;

    @Column(name = "next_scheduled_date", nullable = false)
    private LocalDate nextScheduledDate;

    @Column(nullable = false)
    private Boolean active = true;

    private String notes;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", insertable = false, updatable = false)
    private LocalDateTime updatedAt;
}
