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
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class RestUserController implements UserApi {
    private final UserService userService;
    private final UserCacheService userCacheService;
    private final CurrentUserProvider currentUserProvider;

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/admin/update-user/{userId}")
    public ResponseEntity<UserUpdateResponse> adminUpdateUser(@PathVariable String userId, UserUpdateRequest userUpdateRequest) {
        return ResponseEntity.ok(userService.updateUser(userService.findById(UUID.fromString(userId)), userUpdateRequest));
    }

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
    @DeleteMapping("/admin/delete-user/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> adminDeleteUserById(@PathVariable String userId) {
        userService.deleteUser(UUID.fromString(userId));
        return ResponseEntity.noContent().build();
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin/get/all")
    public ResponseEntity<List<Map<String, User>>> getAllUsers() {
        return ResponseEntity.ok(
                userService.findAllUsers().stream()
                        .map(user -> Map.of(user.getId().toString(), UserMapper.toRest(user)))
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
