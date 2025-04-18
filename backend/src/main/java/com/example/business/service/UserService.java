package com.example.business.service;

import com.example.model.dto.user.UserUpdateRequest;
import com.example.model.dto.user.UserUpdateResponse;
import com.example.model.entity.User;
import com.example.model.dto.user.UserRegistrationRequest;
import com.example.model.dto.user.UserRegistrationResponse;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {
    String patchUserAboutMe(User user, String updatedAboutMe);
    UserUpdateResponse updateUser(User user, UserUpdateRequest request);
    UserRegistrationResponse registerUser(UserRegistrationRequest request);
    void deleteUser(UUID uuid);
    Optional<User> findByUsername(String username);
    List<User> findAllUsers();
}
