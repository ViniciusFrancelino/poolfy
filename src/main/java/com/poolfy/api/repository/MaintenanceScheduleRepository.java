package com.poolfy.api.repository;

import com.poolfy.api.entity.MaintenanceSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MaintenanceScheduleRepository extends JpaRepository<MaintenanceSchedule, Long> {
    List<MaintenanceSchedule> findByPoolIdOrderByNextScheduledDateAsc(Long poolId);
}
