package com.example.seeder;

import com.example.rest.generated.model.UserRegistrationRequest;
import com.example.rest.generated.model.UserRegistrationResponse;
import com.example.service.KeycloakService;
import com.example.service.UserService;
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
                userService.deleteUser(user.getId()); // ✅ handles Keycloak + DB + cache
                System.out.println("🗑️ Deleted user: " + user.getUsername());
            } catch (Exception e) {
                System.out.println("⚠️ Failed to delete user '" + user.getUsername() + "': " + e.getMessage());
            }
        });

        System.out.println("🧹 All users deleted.");

        // 🚀 STEP 2: Register 3 test users
        createTestUser("testuser1", "testuser1@example.com", "password123", "Test", "UserOne");
        createTestUser("testuser2", "testuser2@example.com", "password123", "Test", "UserTwo");
        createTestUser("testuser3", "testuser3@example.com", "password123", "Test", "UserThree");
    }

    private void createTestUser(String username, String email, String password, String firstName, String lastName) {
        try {
            UserRegistrationRequest request = new UserRegistrationRequest(
                    username, email, password, firstName, lastName
            );
            UserRegistrationResponse response = userService.registerUser(request);
            System.out.println("✅ Created user: " + response.getUsername());
        } catch (Exception e) {
            System.out.println("❌ Failed to create user '" + username + "': " + e.getMessage());
        }
    }
}
