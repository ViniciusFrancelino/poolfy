package com.poolfy.api.dto.user;

import com.poolfy.api.entity.enums.ExperienceLevel;
import com.poolfy.api.entity.enums.UserType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

public class UserDtos {

    @Getter
    @Setter
    public static class UserProfileResponse {
        private Long id;
        private String firstName;
        private String lastName;
        private String email;
        private UserType userType;
        private ExperienceLevel experienceLevel;
    }

    @Getter
    @Setter
    public static class UpdateProfileRequest {
        @NotBlank private String firstName;
        @NotBlank private String lastName;
        @NotBlank @Email private String email;
        private String password;
    }

    @Getter
    @Setter
    public static class PreferenceResponse {
        private Long id;
        private Long userId;
        private UserType preferredUserType;
        private ExperienceLevel preferredExperienceLevel;
    }

    @Getter
    @Setter
    public static class UpdatePreferenceRequest {
        private UserType preferredUserType;
        private ExperienceLevel preferredExperienceLevel;
    }
}
