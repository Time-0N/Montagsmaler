package com.example.business.service;

import com.example.model.entity.User;
import com.example.model.dto.UserRegistrationRequest;
import com.example.model.dto.UserRegistrationResponse;

import java.util.Optional;

public interface UserService {
    UserRegistrationResponse registerUser(UserRegistrationRequest request);
    Optional<User> findByUsername(String username);
}
