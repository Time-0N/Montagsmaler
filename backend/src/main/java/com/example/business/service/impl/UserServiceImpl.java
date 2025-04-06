package com.example.business.service.impl;

import com.example.business.service.KeycloakService;
import com.example.business.service.UserService;
import com.example.data.repository.UserRepository;
import com.example.model.dto.auth.AuthenticationRequest;
import com.example.model.dto.auth.TokenResponse;
import com.example.model.entity.User;
import com.example.model.dto.user.UserRegistrationRequest;
import com.example.model.dto.user.UserRegistrationResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final KeycloakService keycloakService;

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

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
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
}
