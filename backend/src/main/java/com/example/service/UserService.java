package com.example.service;

import com.example.data.model.entity.User;
import com.example.rest.generated.model.UserRegistrationRequest;
import com.example.rest.generated.model.UserRegistrationResponse;
import com.example.rest.generated.model.UserUpdateRequest;
import com.example.rest.generated.model.UserUpdateResponse;


import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {
    UserUpdateResponse updateUser(User user, UserUpdateRequest request);
    UserRegistrationResponse registerUser(UserRegistrationRequest request);
    void deleteUser(UUID uuid);
    Optional<User> findByUsername(String username);
    User findById(UUID id);
    List<User> findAllUsers();
}
