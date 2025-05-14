package com.example.rest.controller;

import com.example.rest.generated.AuthApi;
import com.example.rest.generated.model.AuthenticationRequest;
import com.example.rest.generated.model.TokenResponse;
import com.example.rest.generated.model.UserRegistrationRequest;
import com.example.rest.generated.model.UserRegistrationResponse;
import com.example.service.KeycloakService;
import com.example.service.UserService;
import com.example.security.annotation.PublicEndpoint;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class RestAuthController implements AuthApi {

    private final UserService userService;
    private final KeycloakService keycloakService;

    @Override
    @PublicEndpoint(summary = "Login")
    @PostMapping("/token")
    public ResponseEntity<TokenResponse> loginUser(@Valid @RequestBody AuthenticationRequest authenticationRequest) {
        TokenResponse tokenResponse = keycloakService.authenticateUser(authenticationRequest);
        return ResponseEntity.ok(tokenResponse);
    }

    @Override
    @PublicEndpoint(summary = "Register")
    @PostMapping("/register")
    public ResponseEntity<UserRegistrationResponse> registerUser(@Valid @RequestBody UserRegistrationRequest userRegistrationRequest) {
        UserRegistrationResponse response = userService.registerUser(userRegistrationRequest);
        return ResponseEntity.ok(response);
    }
}
