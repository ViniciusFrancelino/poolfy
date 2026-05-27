package com.poolfy.api.repository;

import com.poolfy.api.entity.Maintenance;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MaintenanceRepository extends JpaRepository<Maintenance, Long> {
    List<Maintenance> findByUserIdOrderByMaintenanceDateDesc(Long userId);
    List<Maintenance> findByPoolIdOrderByMaintenanceDateDesc(Long poolId);
    List<Maintenance> findByUserIdAndMaintenanceDateBetweenOrderByMaintenanceDateDesc(Long userId, java.time.LocalDate startDate, java.time.LocalDate endDate);
}
