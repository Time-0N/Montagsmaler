package com.example.service;


import com.example.rest.generated.model.AuthenticationRequest;
import com.example.rest.generated.model.TokenResponse;
import com.example.rest.generated.model.UserRegistrationRequest;
import com.example.rest.generated.model.UserUpdateRequest;

public interface KeycloakService {
    String createKeycloakUser(UserRegistrationRequest request);
    TokenResponse authenticateUser(AuthenticationRequest request);
    void deleteKeycloakUser(String keycloakUserId);
    void updateKeycloakUser(String keycloakUserId, UserUpdateRequest request);
}
