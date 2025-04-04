package com.example.business.service;

import com.example.model.dto.AuthenticationRequest;
import com.example.model.dto.TokenResponse;
import com.example.model.dto.UserRegistrationRequest;

public interface KeycloakService {
    String createKeycloakUser(UserRegistrationRequest request);
    TokenResponse authenticateUser(AuthenticationRequest request);
}
