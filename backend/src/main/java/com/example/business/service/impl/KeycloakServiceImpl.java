package com.example.business.service.impl;

import com.example.business.service.KeycloakService;
import com.example.config.KeycloakProperties;
import com.example.model.dto.auth.AuthenticationRequest;
import com.example.model.dto.auth.TokenResponse;
import com.example.model.dto.user.UserRegistrationRequest;
import com.example.model.dto.user.UserUpdateRequest;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.representations.AccessTokenResponse;
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
        try (Keycloak keycloakForAuth = KeycloakBuilder.builder()
                .serverUrl(properties.getAuthServerUrl())
                .realm(properties.getRealm())
                .username(request.username())
                .password(request.password())
                .clientId(properties.getClientId())
                .clientSecret(properties.getClientSecret())
                .grantType(OAuth2Constants.PASSWORD)
                .build())
        {
            AccessTokenResponse accessTokenResponse = keycloakForAuth.tokenManager().getAccessToken();

            return new TokenResponse(
                    accessTokenResponse.getToken(),
                    accessTokenResponse.getRefreshToken(),
                    accessTokenResponse.getExpiresIn(),
                    accessTokenResponse.getRefreshExpiresIn(),
                    accessTokenResponse.getTokenType()
            );
        } catch (Exception e) {
            throw new RuntimeException("Authentication failed: " + e.getMessage(), e);
        }
    }

    @Override
    public void deleteKeycloakUser(String keycloakUserId) {
        try {
            keycloakClient.realm(properties.getRealm())
                    .users()
                    .get(keycloakUserId)
                    .remove();
        } catch (NotFoundException e) {
            System.out.println("Keycloak user not found: " + keycloakUserId);
        } catch (Exception e) {
            System.out.println("Failed to delete Keycloak user: " + e.getMessage());
            throw new RuntimeException("Keycloak user deletion failed", e);
        }
    }

    @Override
    public void updateKeycloakUser(String keycloakUserId, UserUpdateRequest request) throws RuntimeException {
            var userResource = keycloakClient.realm(properties.getRealm())
                    .users()
                    .get(keycloakUserId);

            UserRepresentation userRep = userResource.toRepresentation();

            if (request.username() != null && !request.username().isEmpty()) {
                userRep.setUsername(request.username());
            }
            if (request.email() != null && !request.email().isEmpty()) {
                userRep.setEmail(request.email());
            }
            if (request.firstName() != null && !request.firstName().isEmpty()) {
                userRep.setFirstName(request.firstName());
            }
            if (request.lastName() != null && !request.lastName().isEmpty()) {
                userRep.setLastName(request.lastName());
            }

            userResource.update(userRep);
    }

    private String extractUserId(Response response) {
        String location = response.getHeaderString("Location");
        System.out.println("Status: " + response.getStatus());
        System.out.println("Entity: " + response.readEntity(String.class));
        return location.substring(location.lastIndexOf('/') + 1);
    }
}
