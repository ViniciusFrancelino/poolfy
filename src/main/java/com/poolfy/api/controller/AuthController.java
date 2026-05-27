package com.poolfy.api.controller;

import com.poolfy.api.dto.auth.AuthDtos.ForgotPasswordRequest;
import com.poolfy.api.dto.auth.AuthDtos.LoginRequest;
import com.poolfy.api.dto.auth.AuthDtos.RegisterRequest;
import com.poolfy.api.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public Object register(@Valid @RequestBody RegisterRequest request) {
        return authService.register(request);
    }

    @PostMapping("/login")
    public Object login(@Valid @RequestBody LoginRequest request) {
        return authService.login(request);
    }

    @PostMapping("/forgot-password")
    public Object forgotPassword(@Valid @RequestBody ForgotPasswordRequest request) {
        return authService.forgotPassword(request);
    }
}
