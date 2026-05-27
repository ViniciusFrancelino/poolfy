package com.poolfy.api.dto.alert;

import com.poolfy.api.entity.enums.AlertType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

public class AlertDtos {

    @Getter
    @Setter
    public static class AlertResponse {
        private Long id;
        private Long userId;
        private Long productId;
        private Long poolId;
        private AlertType alertType;
        private String message;
        private Boolean isRead;
        private LocalDateTime createdAt;
    }
}
