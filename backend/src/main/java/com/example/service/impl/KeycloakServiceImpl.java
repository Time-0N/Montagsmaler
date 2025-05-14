package com.example.service.impl;

import com.example.rest.generated.model.AuthenticationRequest;
import com.example.rest.generated.model.TokenResponse;
import com.example.rest.generated.model.UserRegistrationRequest;
import com.example.rest.generated.model.UserUpdateRequest;
import com.example.service.KeycloakService;
import com.example.config.KeycloakProperties;
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
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEnabled(true);
        user.setEmailVerified(true);

        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(request.getPassword());
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
                .username(request.getUsername())
                .password(request.getPassword())
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

            if (request.getUsername() != null && !request.getUsername().isEmpty()) {
                userRep.setUsername(request.getUsername());
            }
            if (request.getEmail() != null && !request.getEmail().isEmpty()) {
                userRep.setEmail(request.getEmail());
            }
            if (request.getFirstName() != null && !request.getFirstName().isEmpty()) {
                userRep.setFirstName(request.getFirstName());
            }
            if (request.getLastName() != null && !request.getLastName().isEmpty()) {
                userRep.setLastName(request.getLastName());
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
