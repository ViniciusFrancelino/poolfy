package com.poolfy.api.service;

import com.poolfy.api.dto.user.UserDtos.PreferenceResponse;
import com.poolfy.api.dto.user.UserDtos.UpdatePreferenceRequest;
import com.poolfy.api.dto.user.UserDtos.UpdateProfileRequest;
import com.poolfy.api.dto.user.UserDtos.UserProfileResponse;
import com.poolfy.api.entity.User;
import com.poolfy.api.entity.UserPreference;
import com.poolfy.api.exception.BusinessException;
import com.poolfy.api.exception.NotFoundException;
import com.poolfy.api.repository.UserPreferenceRepository;
import com.poolfy.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserPreferenceRepository userPreferenceRepository;
    private final PasswordEncoder passwordEncoder;

    public UserProfileResponse getProfile(Long userId) {
        User user = findUser(userId);
        UserProfileResponse response = new UserProfileResponse();
        response.setId(user.getId());
        response.setFirstName(user.getFirstName());
        response.setLastName(user.getLastName());
        response.setEmail(user.getEmail());
        response.setUserType(user.getUserType());
        response.setExperienceLevel(user.getExperienceLevel());
        return response;
    }

    public UserProfileResponse updateProfile(Long userId, UpdateProfileRequest request) {
        User user = findUser(userId);

        if (!user.getEmail().equalsIgnoreCase(request.getEmail()) && userRepository.existsByEmail(request.getEmail())) {
            throw new BusinessException("Já existe outro usuário com esse email.");
        }

        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());

        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        }

        userRepository.save(user);
        return getProfile(userId);
    }

    public PreferenceResponse getPreferences(Long userId) {
        UserPreference preference = userPreferenceRepository.findByUserId(userId)
                .orElseThrow(() -> new NotFoundException("Preferências não encontradas."));

        PreferenceResponse response = new PreferenceResponse();
        response.setId(preference.getId());
        response.setUserId(preference.getUserId());
        response.setPreferredUserType(preference.getPreferredUserType());
        response.setPreferredExperienceLevel(preference.getPreferredExperienceLevel());
        return response;
    }

    public PreferenceResponse updatePreferences(Long userId, UpdatePreferenceRequest request) {
        findUser(userId);

        UserPreference preference = userPreferenceRepository.findByUserId(userId)
                .orElseGet(() -> {
                    UserPreference pref = new UserPreference();
                    pref.setUserId(userId);
                    return pref;
                });

        preference.setPreferredUserType(request.getPreferredUserType());
        preference.setPreferredExperienceLevel(request.getPreferredExperienceLevel());
        userPreferenceRepository.save(preference);
        return getPreferences(userId);
    }

    public User findUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Usuário não encontrado."));
    }
}
