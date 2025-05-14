package com.example.mappers;

import com.example.data.model.entity.User;

public class UserMapper {
    public static com.example.rest.generated.model.User toRest(User entity) {
        if (entity == null) {
            return null;
        }

        return com.example.rest.generated.model.User.builder()
                .username(entity.getUsername())
                .email(entity.getEmail())
                .firstName(entity.getFirstName())
                .lastName(entity.getLastName())
                .gameWebSocketSessionId(entity.getGameWebSocketSessionId())
                .aboutMe(entity.getAboutMe())
                .build();
    }
}
