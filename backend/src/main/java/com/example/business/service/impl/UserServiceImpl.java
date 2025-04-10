package com.example.business.service.impl;

import com.example.business.service.KeycloakService;
import com.example.business.service.UserService;
import com.example.data.repository.UserRepository;
import com.example.model.dto.auth.AuthenticationRequest;
import com.example.model.dto.auth.TokenResponse;
import com.example.model.dto.user.UserUpdateRequest;
import com.example.model.dto.user.UserUpdateResponse;
import com.example.model.entity.User;
import com.example.model.dto.user.UserRegistrationRequest;
import com.example.model.dto.user.UserRegistrationResponse;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
    public String patchUserAboutMe(User user, String updatedAboutMe) {
        user.setAboutMe(updatedAboutMe);
        return updatedAboutMe;
    }

    @Override
    public UserUpdateResponse updateUser(User user, UserUpdateRequest request) {
        if (!request.username().isEmpty() && !request.username().equals(user.getUsername()) &&
                userRepository.existsByUsername(request.username())) {
            throw new RuntimeException("Username is already taken");
        }

        if (!request.email().isEmpty() && !request.email().equals(user.getEmail()) &&
                userRepository.existsByEmail(request.email())) {
            throw new RuntimeException("Email is already taken");
        }

        updateIfNotEmpty(request.username(), user::setUsername);
        updateIfNotEmpty(request.email(), user::setEmail);
        updateIfNotEmpty(request.firstName(), user::setFirstName);
        updateIfNotEmpty(request.lastName(), user::setLastName);

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
                user.getLastName()
        );
    }

    @Transactional
    @Override
    public UserRegistrationResponse registerUser(UserRegistrationRequest request) {
        if (userRepository.existsByUsernameOrEmail(request.username(), request.email())) {
            throw new RuntimeException("Username or email address already in use");
        }

        String keycloakUserId = keycloakService.createKeycloakUser(request);
        User user = createLocalUser(request, keycloakUserId);

        TokenResponse tokenResponse = keycloakService.authenticateUser(
                new AuthenticationRequest(
                        request.username(),
                        request.password()
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
    @Transactional
    public void deleteUser(UUID uuid) {
        User user = userRepository.findById(uuid)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + uuid));

        keycloakService.deleteKeycloakUser(user.getKeycloakId());
        userRepository.delete(user);
    }

    private User createLocalUser(UserRegistrationRequest request, String keycloakUserId) {
        User user = new User();
        user.setUsername(request.username());
        user.setEmail(request.email());
        user.setFirstName(request.firstName());
        user.setLastName(request.lastName());
        user.setKeycloakId(keycloakUserId);
        return userRepository.save(user);
    }

    private void updateIfNotEmpty(String value, Consumer<String> setter) {
        if (value != null && !value.isEmpty()) {
            setter.accept(value);
        }
    }
}
