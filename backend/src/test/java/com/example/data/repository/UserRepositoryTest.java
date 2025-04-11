package com.example.data.repository;

import com.example.model.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@TestPropertySource(properties = {
        "spring.liquibase.enabled=false"
})
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("Should save and find user by username")
    void findByUsername_ShouldReturnUser() {
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        user.setKeycloakId("kc123");

        userRepository.save(user);

        Optional<User> found = userRepository.findByUsername("testuser");

        assertThat(found).isPresent();
        assertThat(found.get().getEmail()).isEqualTo("test@example.com");
    }

    @Test
    @DisplayName("Should return true if user with username or email exists")
    void existsByUsernameOrEmail_ShouldReturnTrue() {
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setUsername("username");
        user.setEmail("email@example.com");
        user.setKeycloakId("kc456");

        userRepository.save(user);

        boolean exists = userRepository.existsByUsernameOrEmail("username", "email@example.com");

        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("Should return user by keycloakId")
    void findByKeycloakId_ShouldReturnUser() {
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setUsername("keycloakuser");
        user.setEmail("kc@example.com");
        user.setKeycloakId("keycloak-123");

        userRepository.save(user);

        Optional<User> found = userRepository.findByKeycloakId("keycloak-123");

        assertThat(found).isPresent();
        assertThat(found.get().getUsername()).isEqualTo("keycloakuser");
    }

    @Test
    @DisplayName("Should return true if email exists")
    void existsByEmail_ShouldReturnTrue() {
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setUsername("uniqueuser");
        user.setEmail("unique@example.com");
        user.setKeycloakId("kc789");

        userRepository.save(user);

        boolean exists = userRepository.existsByEmail("unique@example.com");

        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("Should return true if username exists")
    void existsByUsername_ShouldReturnTrue() {
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setUsername("checkme");
        user.setEmail("checkme@example.com");
        user.setKeycloakId("kc321");

        userRepository.save(user);

        boolean exists = userRepository.existsByUsername("checkme");

        assertThat(exists).isTrue();
    }
}
