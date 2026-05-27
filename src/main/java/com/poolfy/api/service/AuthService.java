package com.poolfy.api.service;

import com.poolfy.api.dto.auth.AuthDtos.AuthResponse;
import com.poolfy.api.dto.auth.AuthDtos.ForgotPasswordRequest;
import com.poolfy.api.dto.auth.AuthDtos.LoginRequest;
import com.poolfy.api.dto.auth.AuthDtos.RegisterRequest;
import com.poolfy.api.entity.User;
import com.poolfy.api.entity.UserPreference;
import com.poolfy.api.exception.BusinessException;
import com.poolfy.api.repository.UserPreferenceRepository;
import com.poolfy.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final UserPreferenceRepository userPreferenceRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthResponse register(RegisterRequest request) {
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new BusinessException("Password e confirmPassword são diferentes.");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BusinessException("Já existe usuário com esse email.");
        }

        User user = new User();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setUserType(request.getUserType());
        user.setExperienceLevel(request.getExperienceLevel());

        user = userRepository.save(user);

        UserPreference preference = new UserPreference();
        preference.setUserId(user.getId());
        preference.setPreferredUserType(user.getUserType());
        preference.setPreferredExperienceLevel(user.getExperienceLevel());
        userPreferenceRepository.save(preference);

        AuthResponse response = new AuthResponse();
        response.setUserId(user.getId());
        response.setFullName(user.getFirstName() + " " + user.getLastName());
        response.setEmail(user.getEmail());
        response.setMessage("Usuário cadastrado com sucesso.");
        return response;
    }

    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BusinessException("Email ou senha inválidos."));

        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new BusinessException("Email ou senha inválidos.");
        }

        AuthResponse response = new AuthResponse();
        response.setUserId(user.getId());
        response.setFullName(user.getFirstName() + " " + user.getLastName());
        response.setEmail(user.getEmail());
        response.setMessage(Boolean.TRUE.equals(request.getRememberSession())
                ? "Login realizado com lembrar sessão."
                : "Login realizado com sucesso.");
        return response;
    }

    public AuthResponse forgotPassword(ForgotPasswordRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BusinessException("Email não encontrado."));

        AuthResponse response = new AuthResponse();
        response.setUserId(user.getId());
        response.setFullName(user.getFirstName() + " " + user.getLastName());
        response.setEmail(user.getEmail());
        response.setMessage("Solicitação recebida. Fluxo inicial criado, mas o banco atual não possui tabela de token de recuperação.");
        return response;
    }
}
