package com.example.security;

import com.example.data.repository.UserRepository;
import com.example.model.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
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

        var authorities = roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .collect(Collectors.toList());

        String keycloakId = jwt.getSubject();


        //Change this to throw an error when no User can be Found!
        User user = userCache.getOrLoad(keycloakId, () -> userRepository.findByKeycloakId(keycloakId)
                .orElseGet(() -> {
                    User newUser = new User();
                    newUser.setKeycloakId(keycloakId);
                    newUser.setUsername(jwt.getClaim("preferred_username"));
                    newUser.setEmail(jwt.getClaim("email"));
                    newUser.setFirstName(jwt.getClaim("given_name"));
                    newUser.setLastName(jwt.getClaim("family_name"));
                    return userRepository.save(newUser);
                }));

        return new UserAuthentication(user, authorities);
    }
}
