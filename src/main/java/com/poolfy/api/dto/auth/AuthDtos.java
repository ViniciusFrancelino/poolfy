package com.poolfy.api.dto.auth;

import com.poolfy.api.entity.enums.ExperienceLevel;
import com.poolfy.api.entity.enums.UserType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

public class AuthDtos {

    @Getter
    @Setter
    public static class RegisterRequest {
        @NotBlank private String firstName;
        @NotBlank private String lastName;
        @NotBlank @Email private String email;
        @NotBlank private String password;
        @NotBlank private String confirmPassword;
        private UserType userType;
        private ExperienceLevel experienceLevel;
    }

    @Getter
    @Setter
    public static class LoginRequest {
        @NotBlank @Email private String email;
        @NotBlank private String password;
        private Boolean rememberSession;
    }

    @Getter
    @Setter
    public static class ForgotPasswordRequest {
        @NotBlank @Email private String email;
    }

    @Getter
    @Setter
    public static class AuthResponse {
        private Long userId;
        private String fullName;
        private String email;
        private String message;
    }
}
