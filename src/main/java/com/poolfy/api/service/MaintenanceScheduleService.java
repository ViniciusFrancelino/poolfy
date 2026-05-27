package com.poolfy.api.service;

import com.poolfy.api.dto.schedule.ScheduleDtos.CreateOrUpdateScheduleRequest;
import com.poolfy.api.dto.schedule.ScheduleDtos.ScheduleResponse;
import com.poolfy.api.entity.MaintenanceSchedule;
import com.poolfy.api.exception.NotFoundException;
import com.poolfy.api.repository.MaintenanceScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MaintenanceScheduleService {

    private final MaintenanceScheduleRepository maintenanceScheduleRepository;
    private final PoolService poolService;
    private final UserService userService;

    public List<ScheduleResponse> listByPool(Long poolId) {
        poolService.findPool(poolId);
        return maintenanceScheduleRepository.findByPoolIdOrderByNextScheduledDateAsc(poolId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public ScheduleResponse create(CreateOrUpdateScheduleRequest request) {
        poolService.findPool(request.getPoolId());
        userService.findUser(request.getUserId());

        MaintenanceSchedule schedule = new MaintenanceSchedule();
        fill(schedule, request);
        schedule = maintenanceScheduleRepository.save(schedule);
        return toResponse(schedule);
    }

    public ScheduleResponse update(Long scheduleId, CreateOrUpdateScheduleRequest request) {
        MaintenanceSchedule schedule = maintenanceScheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new NotFoundException("Agendamento não encontrado."));
        fill(schedule, request);
        schedule = maintenanceScheduleRepository.save(schedule);
        return toResponse(schedule);
    }

    public ScheduleResponse deactivate(Long scheduleId) {
        MaintenanceSchedule schedule = maintenanceScheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new NotFoundException("Agendamento não encontrado."));
        schedule.setActive(false);
        schedule = maintenanceScheduleRepository.save(schedule);
        return toResponse(schedule);
    }

    private void fill(MaintenanceSchedule schedule, CreateOrUpdateScheduleRequest request) {
        schedule.setPoolId(request.getPoolId());
        schedule.setUserId(request.getUserId());
        schedule.setScheduleType(request.getScheduleType());
        schedule.setFrequencyDays(request.getFrequencyDays());
        schedule.setNextScheduledDate(request.getNextScheduledDate());
        schedule.setActive(request.getActive() == null ? true : request.getActive());
        schedule.setNotes(request.getNotes());
    }

    private ScheduleResponse toResponse(MaintenanceSchedule schedule) {
        ScheduleResponse response = new ScheduleResponse();
        response.setId(schedule.getId());
        response.setPoolId(schedule.getPoolId());
        response.setUserId(schedule.getUserId());
        response.setScheduleType(schedule.getScheduleType());
        response.setFrequencyDays(schedule.getFrequencyDays());
        response.setNextScheduledDate(schedule.getNextScheduledDate());
        response.setActive(schedule.getActive());
        response.setNotes(schedule.getNotes());
        return response;
    }
}
