package com.poolfy.api.repository;

import com.poolfy.api.entity.Pool;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PoolRepository extends JpaRepository<Pool, Long> {
    List<Pool> findByUserIdOrderByCreatedAtDesc(Long userId);
}
