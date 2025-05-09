package com.example.mappers;

import com.example.model.entity.User;

public class UserMapper {
    public static com.example.rest.controller.generated.model.User toRest(User businessType) {
        if (businessType == null) {
            return null;
        }
        com.example.rest.controller.generated.model.User rest = new com.example.rest.controller.generated.model.User();
        rest.setId(businessType.getId());
        rest.setUsername(businessType.getUsername());
        rest.setEmail(businessType.getEmail());
        rest.setFirstName(businessType.getFirstName());
        rest.setLastName(businessType.getLastName());
        rest.setGameWebSocketSessionId(businessType.getGameWebSocketSessionId());
        rest.setAboutMe(businessType.getAboutMe());
        rest.keycloakId(businessType.getKeycloakId());
        return rest;
    }

    public static User toBusinessType(com.example.rest.controller.generated.model.User rest) {
        if (rest == null) {
            return null;
        }
        User businessType = new User();
        businessType.setId(rest.getId());
        businessType.setUsername(rest.getUsername());
        businessType.setEmail(rest.getEmail());
        businessType.setFirstName(rest.getFirstName());
        businessType.setLastName(rest.getLastName());
        businessType.setGameWebSocketSessionId(rest.getGameWebSocketSessionId());
        businessType.setAboutMe(businessType.getAboutMe());
        businessType.setKeycloakId(businessType.getKeycloakId());
        return businessType;
    }

}
