package com.example.mappers;

import com.example.data.model.dao.GameSession;

public class GameSessionMapper {
    public static com.example.rest.generated.model.GameSession toRest(GameSession entity) {
        if (entity == null) {
            return null;
        }

        return com.example.rest.generated.model.GameSession.builder()
                .roomId(entity.getRoomId())
                .users(entity.getUsers().stream()
                        .map(UserMapper::toRest)
                        .toList()
                )
                .readyStatus(entity.getReadyStatus())
                .submittedWords(entity.getSubmittedWords())
                .drawingOrder(entity.getDrawingOrder().stream()
                        .map(UserMapper::toRest)
                        .toList()
                )
                .currentDrawerIndex(entity.getCurrentDrawerIndex())
                .phase(com.example.rest.generated.model.GamePhase.valueOf(entity.getPhase().name()))
                .currentWord(entity.getCurrentWord())
                .currentDrawer(UserMapper.toRest(entity.getCurrentDrawer()))
                .timerSeconds(entity.getTimerSeconds())
                .build();
    }
}
