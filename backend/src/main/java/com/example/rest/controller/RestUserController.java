package com.example.rest.controller;

import com.example.mappers.UserMapper;
import com.example.rest.generated.UserApi;
import com.example.rest.generated.model.User;
import com.example.rest.generated.model.UserUpdateRequest;
import com.example.rest.generated.model.UserUpdateResponse;
import com.example.security.util.CurrentUserProvider;
import com.example.service.UserService;
import com.example.security.UserCacheService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class RestUserController implements UserApi {
    private final UserService userService;
    private final UserCacheService userCacheService;
    private final CurrentUserProvider currentUserProvider;

    @Override
    @DeleteMapping("/delete/User")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteUser() {
        com.example.data.model.entity.User currentUser = currentUserProvider.getCurrentUser();
        userService.deleteUser(currentUser.getId());
        userCacheService.evict(currentUser.getKeycloakId());

        return ResponseEntity.noContent().build();
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/get/all")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(
                userService.findAllUsers().stream()
                        .map(UserMapper::toRest)
                        .toList()
        );
    }

    @Override
    @GetMapping("/me")
    public ResponseEntity<User> getCurrentUser() {
        com.example.data.model.entity.User currentUser = currentUserProvider.getCurrentUser();
        return ResponseEntity.ok(UserMapper.toRest(currentUser));
    }

    @Override
    @PutMapping("/update/user")
    public ResponseEntity<UserUpdateResponse> updateUser(UserUpdateRequest userUpdateRequest) {
        com.example.data.model.entity.User currentUser = currentUserProvider.getCurrentUser();
        return ResponseEntity.ok(userService.updateUser(currentUser, userUpdateRequest));
    }
}
