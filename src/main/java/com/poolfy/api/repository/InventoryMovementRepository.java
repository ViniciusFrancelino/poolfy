package com.poolfy.api.repository;

import com.poolfy.api.entity.InventoryMovement;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface InventoryMovementRepository extends JpaRepository<InventoryMovement, Long> {
    List<InventoryMovement> findByUserIdOrderByCreatedAtDesc(Long userId);
}
