package com.example.model.dto.user;

import com.example.model.dto.auth.TokenResponse;

public record UserRegistrationResponse(
        String username,
        String email,
        TokenResponse token
) {}
