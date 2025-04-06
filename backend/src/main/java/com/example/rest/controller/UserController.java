package com.example.rest.controller;

import com.example.business.service.UserService;
import com.example.model.entity.User;
import com.example.security.CurrentUser;
import com.example.security.UserCacheService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserCacheService userCacheService;

    //Needs rewriting! Used for dev atm
    @GetMapping("/me")
    public User getMe(@CurrentUser User user) {
        return user;
    }

    @DeleteMapping("/delete/me")
    public void deleteMe(@CurrentUser User user) {
        userService.deleteUser(user.getId());
        userCacheService.evict(user.getKeycloakId());
    }
}
