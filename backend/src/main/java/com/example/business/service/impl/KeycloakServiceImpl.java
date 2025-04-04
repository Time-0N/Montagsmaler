package com.example.business.service.impl;

import com.example.business.service.KeycloakService;
import com.example.config.KeycloakProperties;
import com.example.model.dto.AuthenticationRequest;
import com.example.model.dto.TokenResponse;
import com.example.model.dto.UserRegistrationRequest;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class KeycloakServiceImpl implements KeycloakService {
    private final Keycloak keycloakClient;
    private final KeycloakProperties properties;

    @Override
    public String createKeycloakUser(UserRegistrationRequest request) {
        UserRepresentation user = new UserRepresentation();
        user.setUsername(request.username());
        user.setEmail(request.email());
        user.setFirstName(request.firstName());
        user.setLastName(request.lastName());
        user.setEnabled(true);
        user.setEmailVerified(true);

        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(request.password());
        credential.setTemporary(false);

        user.setCredentials(List.of(credential));

        Response response = keycloakClient.realm(properties.getRealm())
                .users()
                .create(user);
        return extractUserId(response);
    }

    @Override
    public TokenResponse authenticateUser(AuthenticationRequest request) {
        return null;
    }

    private String extractUserId(Response response) {
        String location = response.getHeaderString("Location");
        return location.substring(location.lastIndexOf('/') + 1);
    }
}
