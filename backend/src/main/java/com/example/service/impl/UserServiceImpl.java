package com.example.service.impl;

import com.example.rest.generated.model.*;
import com.example.service.KeycloakService;
import com.example.service.UserService;
import com.example.data.repository.UserRepository;
import com.example.data.model.entity.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final KeycloakService keycloakService;


    @Override
    public UserUpdateResponse updateUser(User user, UserUpdateRequest request) {
        if (StringUtils.hasText(request.getUsername()) &&
                !request.getUsername().equals(user.getUsername()) &&
                userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username is already taken");
        }

        if (StringUtils.hasText(request.getEmail()) &&
                !request.getEmail().equals(user.getEmail()) &&
                userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email is already taken");
        }

        updateIfNotEmpty(request.getUsername(), user::setUsername);
        updateIfNotEmpty(request.getEmail(), user::setEmail);
        updateIfNotEmpty(request.getFirstName(), user::setFirstName);
        updateIfNotEmpty(request.getLastName(), user::setLastName);
        updateIfNotEmpty(request.getAboutMe(), user::setAboutMe);

        try {
            keycloakService.updateKeycloakUser(user.getKeycloakId(), request);
        } catch (Exception e) {
            throw new RuntimeException("Failed to update Keycloak user", e);
        }

        userRepository.save(user);

        return new UserUpdateResponse(
                user.getUsername(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getAboutMe()
        );
    }

    @Transactional
    @Override
    public UserRegistrationResponse registerUser(UserRegistrationRequest request) {
        if (userRepository.existsByUsernameOrEmail(request.getUsername(), request.getEmail())) {
            throw new RuntimeException("Username or email address already in use");
        }

        String keycloakUserId = keycloakService.createKeycloakUser(request);
        User user = createLocalUser(request, keycloakUserId);

        TokenResponse tokenResponse = keycloakService.authenticateUser(
                new AuthenticationRequest(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        return new UserRegistrationResponse(
                user.getUsername(),
                user.getEmail(),
                tokenResponse
        );
    }

    @Override
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    @Transactional
    public void deleteUser(UUID uuid) {
        User user = userRepository.findById(uuid)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + uuid));

        keycloakService.deleteKeycloakUser(user.getKeycloakId());
        userRepository.delete(user);
    }

    private User createLocalUser(UserRegistrationRequest request, String keycloakUserId) {
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setKeycloakId(keycloakUserId);
        return userRepository.save(user);
    }

    private void updateIfNotEmpty(String value, Consumer<String> setter) {
        if (value != null && !value.isEmpty()) {
            setter.accept(value);
        }
    }
}
