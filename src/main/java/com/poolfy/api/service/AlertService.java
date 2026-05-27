package com.poolfy.api.service;

import com.poolfy.api.dto.alert.AlertDtos.AlertResponse;
import com.poolfy.api.entity.Alert;
import com.poolfy.api.entity.enums.AlertType;
import com.poolfy.api.exception.NotFoundException;
import com.poolfy.api.repository.AlertRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AlertService {

    private final AlertRepository alertRepository;

    public List<AlertResponse> listByUser(Long userId) {
        return alertRepository.findByUserIdOrderByCreatedAtDesc(userId).stream().map(this::toResponse).toList();
    }

    public AlertResponse markAsRead(Long alertId) {
        Alert alert = alertRepository.findById(alertId)
                .orElseThrow(() -> new NotFoundException("Alerta não encontrado."));
        alert.setIsRead(true);
        alert = alertRepository.save(alert);
        return toResponse(alert);
    }

    public void createLowStockAlert(Long userId, Long productId, String message) {
        Alert alert = new Alert();
        alert.setUserId(userId);
        alert.setProductId(productId);
        alert.setAlertType(AlertType.LOW_STOCK);
        alert.setMessage(message);
        alert.setIsRead(false);
        alertRepository.save(alert);
    }

    public long countUnread(Long userId) {
        return alertRepository.countByUserIdAndIsReadFalse(userId);
    }

    private AlertResponse toResponse(Alert alert) {
        AlertResponse response = new AlertResponse();
        response.setId(alert.getId());
        response.setUserId(alert.getUserId());
        response.setProductId(alert.getProductId());
        response.setPoolId(alert.getPoolId());
        response.setAlertType(alert.getAlertType());
        response.setMessage(alert.getMessage());
        response.setIsRead(alert.getIsRead());
        response.setCreatedAt(alert.getCreatedAt());
        return response;
    }
}
