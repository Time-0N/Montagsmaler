package com.example.business.service;

import com.example.model.dto.auth.AuthenticationRequest;
import com.example.model.dto.auth.TokenResponse;
import com.example.model.dto.user.UserRegistrationRequest;
import com.example.model.dto.user.UserUpdateRequest;

public interface KeycloakService {
    String createKeycloakUser(UserRegistrationRequest request);
    TokenResponse authenticateUser(AuthenticationRequest request);
    void deleteKeycloakUser(String keycloakUserId);
    void updateKeycloakUser(String keycloakUserId, UserUpdateRequest request);
}
