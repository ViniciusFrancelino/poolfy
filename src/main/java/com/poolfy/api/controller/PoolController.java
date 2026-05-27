package com.poolfy.api.controller;

import com.poolfy.api.dto.pool.PoolDtos.CreateOrUpdatePoolRequest;
import com.poolfy.api.dto.common.ApiMessageResponse;
import com.poolfy.api.service.PoolService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pools")
@RequiredArgsConstructor
public class PoolController {

    private final PoolService poolService;

    @GetMapping
    public Object list(@RequestParam Long userId) {
        return poolService.listByUser(userId);
    }

    @GetMapping("/{poolId}")
    public Object get(@PathVariable Long poolId) {
        return poolService.getById(poolId);
    }

    @GetMapping("/{poolId}/details")
    public Object details(@PathVariable Long poolId) {
        return poolService.getDetails(poolId);
    }

    @PostMapping
    public Object create(@RequestBody CreateOrUpdatePoolRequest request) {
        return poolService.create(request);
    }

    @PutMapping("/{poolId}")
    public Object update(@PathVariable Long poolId, @RequestBody CreateOrUpdatePoolRequest request) {
        return poolService.update(poolId, request);
    }

    @DeleteMapping("/{poolId}")
    public Object delete(@PathVariable Long poolId) {
        poolService.delete(poolId);
        return new ApiMessageResponse("Piscina removida com sucesso.");
    }
}
