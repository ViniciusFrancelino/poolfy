package com.poolfy.api.controller;

import com.poolfy.api.dto.user.UserDtos.UpdatePreferenceRequest;
import com.poolfy.api.dto.user.UserDtos.UpdateProfileRequest;
import com.poolfy.api.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/{userId}/profile")
    public Object getProfile(@PathVariable Long userId) {
        return userService.getProfile(userId);
    }

    @PutMapping("/{userId}/profile")
    public Object updateProfile(@PathVariable Long userId, @Valid @RequestBody UpdateProfileRequest request) {
        return userService.updateProfile(userId, request);
    }

    @GetMapping("/{userId}/preferences")
    public Object getPreferences(@PathVariable Long userId) {
        return userService.getPreferences(userId);
    }

    @PutMapping("/{userId}/preferences")
    public Object updatePreferences(@PathVariable Long userId, @RequestBody UpdatePreferenceRequest request) {
        return userService.updatePreferences(userId, request);
    }
}
