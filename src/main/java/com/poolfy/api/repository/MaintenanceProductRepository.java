package com.poolfy.api.repository;

import com.poolfy.api.entity.MaintenanceProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MaintenanceProductRepository extends JpaRepository<MaintenanceProduct, Long> {
    List<MaintenanceProduct> findByMaintenanceId(Long maintenanceId);
}
