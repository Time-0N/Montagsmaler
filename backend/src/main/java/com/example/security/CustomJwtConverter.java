package com.example.security;

import com.example.data.repository.UserRepository;
import com.example.data.model.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CustomJwtConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    private final UserRepository userRepository;
    private final UserCacheService userCache;

    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {
        Map<String, Object> realmAccess = jwt.getClaimAsMap("realm_access");
        Collection<String> roles = (Collection<String>) realmAccess.getOrDefault("roles", Collections.emptyList());

        Map<String, Object> resourceAccess = jwt.getClaimAsMap("resource_access");
        if (resourceAccess != null && resourceAccess.containsKey("dev")) {
            Map<String, Object> devAccess = (Map<String, Object>) resourceAccess.get("dev");
            Collection<String> clientRoles = (Collection<String>) devAccess.getOrDefault("roles", Collections.emptyList());
            roles.addAll(clientRoles);
        }

        var authorities = roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .collect(Collectors.toList());

        String keycloakId = jwt.getSubject();

        User user = userCache.getOrLoad(keycloakId, () -> userRepository.findByKeycloakId(keycloakId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found in DB for keycloakId: " + keycloakId))
        );

        return new UserAuthentication(user, authorities);
    }
}
