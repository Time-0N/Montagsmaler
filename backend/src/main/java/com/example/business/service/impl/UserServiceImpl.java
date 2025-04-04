package com.example.business.service.impl;

import com.example.business.service.KeycloakService;
import com.example.business.service.UserService;
import com.example.data.repository.UserRepository;
import com.example.model.entity.User;
import com.example.model.dto.UserRegistrationRequest;
import com.example.model.dto.UserRegistrationResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final KeycloakService keycloakAdminService;

    @Transactional
    @Override
    public UserRegistrationResponse registerUser(UserRegistrationRequest request) {
        if (userRepository.existsByUsernameOrEmail(request.username(), request.email())) {
            throw new RuntimeException("Username or email address already in use");
        }

        String keycloakUserId = keycloakAdminService.createKeycloakUser(request);
        User user = createLocalUser(request, keycloakUserId);

        return new UserRegistrationResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail()
        );
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
