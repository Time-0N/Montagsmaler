package com.example.rest.controller;

import com.example.business.service.UserService;
import com.example.model.dto.user.UserUpdateRequest;
import com.example.model.dto.user.UserUpdateResponse;
import com.example.model.entity.User;
import com.example.rest.generated.UserApi;
import com.example.security.annotation.CurrentUser;
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

    //Needs rewriting! Used for dev atm
    @GetMapping("/me")
    public User getMe(@CurrentUser User user) {
        return user;
    }

    @PutMapping("/update/user")
    public ResponseEntity<UserUpdateResponse> updateUser(
            @CurrentUser User user,
            @RequestBody UserUpdateRequest request
            ) {
        return ResponseEntity.ok(userService.updateUser(user, request));
    }

    @DeleteMapping("/delete/User")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@CurrentUser User user) {
        userService.deleteUser(user.getId());
        userCacheService.evict(user.getKeycloakId());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/get/all")
    public ResponseEntity<List<com.example.rest.controller.generated.model.User>> getAllUsers() {
        return ResponseEntity.ok(
                userService.findAllUsers().stream()
                        .map(com.example.mappers.UserMapper::toRest)
                        .toList()

        );
    }
}
