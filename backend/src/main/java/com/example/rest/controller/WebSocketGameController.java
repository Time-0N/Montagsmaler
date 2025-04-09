package com.example.rest.controller;

import com.example.business.service.GameService;
import com.example.model.dto.game.DrawingData;
import com.example.model.entity.User;
import com.example.security.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class WebSocketGameController {
    private final GameService gameService;

    @MessageMapping("/game/{roomId}/guess")
    public void handleGuess(
            @CurrentUser User user,
            @Payload String guess,
            @DestinationVariable String roomId,
            SimpMessageHeaderAccessor headerAccessor
    ) {
        gameService.processGuess(
                user,
                guess,
                roomId,
                headerAccessor.getSessionId()
        );
    }

    @MessageMapping("/game/{roomId}/draw")
    public void handleDrawing(
            @CurrentUser User user,
            @Payload DrawingData data,
            @DestinationVariable String roomId
    ) {
        data.setSenderSessionId(user.getId().toString());
        gameService.broadcastDrawing(data);
    }
}
