package com.poolfy.api.service;

import com.poolfy.api.entity.RecentActivity;
import com.poolfy.api.entity.enums.ActivityType;
import com.poolfy.api.repository.RecentActivityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ActivityService {

    private final RecentActivityRepository recentActivityRepository;

    public void register(Long userId, ActivityType activityType, Long referenceId, String description) {
        RecentActivity activity = new RecentActivity();
        activity.setUserId(userId);
        activity.setActivityType(activityType);
        activity.setReferenceId(referenceId);
        activity.setDescription(description);
        recentActivityRepository.save(activity);
    }
}
