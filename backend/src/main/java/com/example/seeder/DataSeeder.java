package com.example.seeder;

import com.example.business.service.KeycloakService;
import com.example.data.repository.UserRepository;
import com.example.model.dto.UserRegistrationRequest;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("dev")
@RequiredArgsConstructor
public class DataSeeder {

    private final UserRepository userRepository;
    private final KeycloakService keycloakService;

    @PostConstruct
    public void resetAndSeed() {
        userRepository.findAll().forEach(user -> {
            try {
                keycloakService.deleteKeycloakUser(user.getKeycloakId());
            } catch (Exception e) {
                System.out.println("⚠️ Failed to delete user in Keycloak: " + e.getMessage());
            }
        });

        userRepository.deleteAll();
        System.out.println("🧹 All users deleted.");

        try {
            UserRegistrationRequest testUser = new UserRegistrationRequest(
                    "testuser",
                    "testuser@example.com",
                    "password123",
                    "Test",
                    "User"
            );

            keycloakService.createKeycloakUser(testUser);
            System.out.println("✅ Test user created.");
        } catch (Exception e) {
            System.out.println("❌ Failed to create test user: " + e.getMessage());
        }
    }
}
