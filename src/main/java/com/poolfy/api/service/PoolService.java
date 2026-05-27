package com.poolfy.api.service;

import com.poolfy.api.dto.pool.PoolDtos.CreateOrUpdatePoolRequest;
import com.poolfy.api.dto.pool.PoolDtos.PoolDetailsResponse;
import com.poolfy.api.dto.pool.PoolDtos.PoolResponse;
import com.poolfy.api.entity.Pool;
import com.poolfy.api.entity.enums.ActivityType;
import com.poolfy.api.exception.BusinessException;
import com.poolfy.api.exception.NotFoundException;
import com.poolfy.api.repository.MaintenanceProductRepository;
import com.poolfy.api.repository.MaintenanceRepository;
import com.poolfy.api.repository.MaintenanceScheduleRepository;
import com.poolfy.api.repository.PoolRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PoolService {

    private final PoolRepository poolRepository;
    private final MaintenanceRepository maintenanceRepository;
    private final MaintenanceProductRepository maintenanceProductRepository;
    private final MaintenanceScheduleRepository maintenanceScheduleRepository;
    private final ActivityService activityService;
    private final UserService userService;

    public List<PoolResponse> listByUser(Long userId) {
        userService.findUser(userId);
        return poolRepository.findByUserIdOrderByCreatedAtDesc(userId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public PoolResponse getById(Long poolId) {
        return toResponse(findPool(poolId));
    }

    public PoolDetailsResponse getDetails(Long poolId) {
        Pool pool = findPool(poolId);
        PoolDetailsResponse response = new PoolDetailsResponse();
        response.setPool(toResponse(pool));
        response.setMaintenances(maintenanceRepository.findByPoolIdOrderByMaintenanceDateDesc(poolId));
        response.setSchedules(maintenanceScheduleRepository.findByPoolIdOrderByNextScheduledDateAsc(poolId));
        response.setUsedProducts(
                maintenanceRepository.findByPoolIdOrderByMaintenanceDateDesc(poolId).stream()
                        .flatMap(m -> maintenanceProductRepository.findByMaintenanceId(m.getId()).stream())
                        .toList()
        );
        return response;
    }

    public PoolResponse create(CreateOrUpdatePoolRequest request) {
        userService.findUser(request.getUserId());

        Pool pool = new Pool();
        fillPool(pool, request);
        pool = poolRepository.save(pool);

        activityService.register(pool.getUserId(), ActivityType.POOL_CREATED, pool.getId(),
                "Piscina cadastrada: " + pool.getName());

        return toResponse(pool);
    }

    public PoolResponse update(Long poolId, CreateOrUpdatePoolRequest request) {
        Pool pool = findPool(poolId);
        fillPool(pool, request);
        pool = poolRepository.save(pool);

        activityService.register(pool.getUserId(), ActivityType.POOL_UPDATED, pool.getId(),
                "Piscina atualizada: " + pool.getName());

        return toResponse(pool);
    }

    public void delete(Long poolId) {
        Pool pool = findPool(poolId);
        poolRepository.delete(pool);

        activityService.register(pool.getUserId(), ActivityType.POOL_DELETED, poolId,
                "Piscina removida: " + pool.getName());
    }

    public Pool findPool(Long poolId) {
        return poolRepository.findById(poolId)
                .orElseThrow(() -> new NotFoundException("Piscina não encontrada."));
    }

    private void fillPool(Pool pool, CreateOrUpdatePoolRequest request) {
        if (request.getWidthM() == null || request.getLengthM() == null || request.getDepthM() == null) {
            throw new BusinessException("Largura, comprimento e profundidade são obrigatórios para cálculo automático de volume.");
        }

        pool.setUserId(request.getUserId());
        pool.setName(request.getName());
        pool.setWidthM(request.getWidthM());
        pool.setLengthM(request.getLengthM());
        pool.setDepthM(request.getDepthM());
        pool.setShape(request.getShape());
        pool.setNotes(request.getNotes());
        pool.setNextMaintenanceDate(request.getNextMaintenanceDate());
        pool.setVolumeM3(calculateVolume(request.getWidthM(), request.getLengthM(), request.getDepthM()));
    }

    private BigDecimal calculateVolume(BigDecimal width, BigDecimal length, BigDecimal depth) {
        return width.multiply(length).multiply(depth).setScale(2, RoundingMode.HALF_UP);
    }

    private PoolResponse toResponse(Pool pool) {
        PoolResponse response = new PoolResponse();
        response.setId(pool.getId());
        response.setUserId(pool.getUserId());
        response.setName(pool.getName());
        response.setWidthM(pool.getWidthM());
        response.setLengthM(pool.getLengthM());
        response.setDepthM(pool.getDepthM());
        response.setVolumeM3(pool.getVolumeM3());
        response.setShape(pool.getShape());
        response.setNotes(pool.getNotes());
        response.setLastMaintenanceDate(pool.getLastMaintenanceDate());
        response.setNextMaintenanceDate(pool.getNextMaintenanceDate());
        return response;
    }
}
