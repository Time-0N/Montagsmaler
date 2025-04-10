package com.example.model.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record UserUpdateRequest(
        @Size(min = 3, max = 20) String username,
        @Email String email,
        String firstName,
        String lastName
) {
}
