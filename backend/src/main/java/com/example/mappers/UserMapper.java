package com.example.mappers;

import com.example.model.entity.User;

public class UserMapper {

    public static com.example.rest.controller.generated.model.User toDto(User entity) {
        if (entity == null) return null;

        com.example.rest.controller.generated.model.User dto = new com.example.rest.controller.generated.model.User();
        dto.setId(entity.getId() != null ? entity.getId() : null);
        dto.setUsername(entity.getUsername());
        dto.setEmail(entity.getEmail());
        dto.setFirstName(entity.getFirstName());
        dto.setLastName(entity.getLastName());
        dto.setGameWebSocketSessionId(entity.getGameWebSocketSessionId());
        dto.setAboutMe(entity.getAboutMe());
        dto.setKeycloakId(entity.getKeycloakId());
        return dto;
    }

    public static User toEntity(com.example.rest.controller.generated.model.User dto) {
        if (dto == null) return null;

        User entity = new User();
        entity.setUsername(dto.getUsername());
        entity.setEmail(dto.getEmail());
        entity.setFirstName(dto.getFirstName());
        entity.setLastName(dto.getLastName());
        entity.setGameWebSocketSessionId(dto.getGameWebSocketSessionId());
        entity.setAboutMe(dto.getAboutMe());
        entity.setKeycloakId(dto.getKeycloakId());
        return entity;
    }
}
