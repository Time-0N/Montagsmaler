package com.example.model.dto.user;

public record UserUpdateResponse(
        String username,
        String email,
        String firstname,
        String lastname
) {
}
