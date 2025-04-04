package com.example.rest.controller;

import com.example.business.service.KeycloakService;
import com.example.business.service.UserService;
import com.example.model.dto.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final KeycloakService keycloakService;

    @PostMapping("/register")
    public ResponseEntity<UserRegistrationResponse> registerUser(
            @Valid @RequestBody UserRegistrationRequest request
    ) {
        UserRegistrationResponse response = userService.registerUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/token")
    public ResponseEntity<TokenResponse> getToken(
            @Valid @RequestBody AuthenticationRequest request
    ) {
        TokenResponse tokenResponse = keycloakService.authenticateUser(request);
        return ResponseEntity.ok(tokenResponse);
    }
}
