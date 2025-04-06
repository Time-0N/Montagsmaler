package com.example.model.dto.user;

public record UserProfileResponse(
        String username,
        String email,
        String firstName,
        String lastName
) {}
