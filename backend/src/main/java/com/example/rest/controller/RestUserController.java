package com.example.rest.controller;

import com.example.business.service.UserService;
import com.example.model.dto.user.UserUpdateRequest;
import com.example.model.dto.user.UserUpdateResponse;
import com.example.model.entity.User;
import com.example.security.CurrentUser;
import com.example.security.UserCacheService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class RestUserController {
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

    @PatchMapping("/patch/userAboutMe")
    public ResponseEntity<String> patchUserAboutMe(
            @CurrentUser User user,
            @RequestBody String updatedAboutMe
    ) {
        return ResponseEntity.ok(userService.patchUserAboutMe(user, updatedAboutMe));
    }

    @DeleteMapping("/delete/User")
    public void deleteUser(@CurrentUser User user) {
        userService.deleteUser(user.getId());
        userCacheService.evict(user.getKeycloakId());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/get/all")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.findAllUsers());
    }
}
