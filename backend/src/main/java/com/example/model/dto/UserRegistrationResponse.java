package com.example.model.dto;

import java.util.UUID;

public record UserRegistrationResponse(
        UUID id,
        String username,
        String email
) {}
