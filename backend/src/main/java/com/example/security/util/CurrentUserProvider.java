package com.example.security.util;

import com.example.data.model.entity.User;
import com.example.security.UserAuthentication;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class CurrentUserProvider {

    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication instanceof UserAuthentication userAuth) {
            return (User) userAuth.getPrincipal();
        }

        throw new IllegalStateException("No authenticated user found in SecurityContext");
    }

    public boolean isAuthenticated() {
        return SecurityContextHolder.getContext().getAuthentication().isAuthenticated();
    }
}
