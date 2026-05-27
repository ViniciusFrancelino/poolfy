package com.poolfy.api.repository;

import com.poolfy.api.entity.RecentActivity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface RecentActivityRepository extends JpaRepository<RecentActivity, Long> {
    List<RecentActivity> findTop10ByUserIdOrderByCreatedAtDesc(Long userId);
}
