package com.example.rest.controller;

import com.example.business.service.KeycloakService;
import com.example.business.service.UserService;
import com.example.model.dto.auth.AuthenticationRequest;
import com.example.model.dto.auth.TokenResponse;
import com.example.model.dto.user.UserRegistrationRequest;
import com.example.model.dto.user.UserRegistrationResponse;
import com.example.rest.generated.AuthApi;
import com.example.security.annotation.PublicEndpoint;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class RestAuthController implements AuthApi {

    private final UserService userService;
    private final KeycloakService keycloakService;

    @PublicEndpoint(summary = "Register")
    @PostMapping("/register")
    public ResponseEntity<UserRegistrationResponse> registerUser(
            @Valid @RequestBody UserRegistrationRequest request
    ) {
        UserRegistrationResponse response = userService.registerUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PublicEndpoint(summary = "Login")
    @PostMapping("/token")
    public ResponseEntity<TokenResponse> getToken(
            @Valid @RequestBody AuthenticationRequest request
    ) {
        log.info("Login attempt for user: {}", request.username());
        TokenResponse tokenResponse = keycloakService.authenticateUser(request);
        log.info("Generated token for user: {}", request.username());
        return ResponseEntity.ok(tokenResponse);
    }
}
