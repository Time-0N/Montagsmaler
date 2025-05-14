package com.example.rest.controller;

import com.example.rest.generated.model.DrawingData;
import com.example.service.GameService;
import com.example.data.model.entity.User;
import com.example.security.annotation.CurrentUser;
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
        data.setRoomId(roomId);
        data.setSenderSessionId(user.getId().toString());

        gameService.broadcastDrawing(data);
    }
}
