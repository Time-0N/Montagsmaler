package com.example.config;

import com.example.model.entity.User;
import com.example.security.annotation.CurrentUser;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;
import java.util.UUID;

@Configuration
public class TestWebConfig implements WebMvcConfigurer {

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new HandlerMethodArgumentResolver() {
            @Override
            public boolean supportsParameter(org.springframework.core.MethodParameter parameter) {
                return parameter.hasParameterAnnotation(CurrentUser.class)
                        && parameter.getParameterType().equals(User.class);
            }

            @Override
            public Object resolveArgument(org.springframework.core.MethodParameter parameter,
                                          org.springframework.web.method.support.ModelAndViewContainer mavContainer,
                                          org.springframework.web.context.request.NativeWebRequest webRequest,
                                          org.springframework.web.bind.support.WebDataBinderFactory binderFactory) {

                User user = new User();
                user.setId(UUID.fromString("00000000-0000-0000-0000-000000000000"));
                user.setUsername("testuser");
                user.setEmail("test@example.com");
                user.setKeycloakId("kc123");
                return user;
            }
        });
    }
}
