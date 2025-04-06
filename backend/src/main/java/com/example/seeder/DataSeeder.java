package com.example.seeder;

import com.example.business.service.KeycloakService;
import com.example.business.service.UserService;
import com.example.model.dto.user.UserRegistrationRequest;
import com.example.model.dto.user.UserRegistrationResponse;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("dev")
@RequiredArgsConstructor
public class DataSeeder {

    private final UserService userService;
    private final KeycloakService keycloakService;

    @PostConstruct
    public void resetAndSeed() {
        // 🔥 STEP 1: Delete all users via UserService
        userService.findAllUsers().forEach(user -> {
            try {
                userService.deleteUser(user.getId()); // ✅ this handles Keycloak + DB + cache
                System.out.println("🗑️ Deleted user: " + user.getUsername());
            } catch (Exception e) {
                System.out.println("⚠️ Failed to delete user '" + user.getUsername() + "': " + e.getMessage());
            }
        });

        System.out.println("🧹 All users deleted.");

        // 🚀 STEP 2: Create test user via registration
        try {
            UserRegistrationRequest testUser = new UserRegistrationRequest(
                    "testuser",
                    "testuser@example.com",
                    "password123",
                    "Test",
                    "User"
            );

            UserRegistrationResponse response = userService.registerUser(testUser);
            System.out.println("✅ Test user created: " + response.username());
        } catch (Exception e) {
            System.out.println("❌ Failed to create test user: " + e.getMessage());
        }
    }
}
