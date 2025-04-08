package com.example.rest.controller;

import com.example.business.service.GameService;
import com.example.model.dto.game.DrawingData;
import com.example.model.dto.game.SubmittedWord;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class WebSocketController {
    private final GameService gameService;

    @MessageMapping("/game/submit-word")
    public void submitWord(@Payload SubmittedWord word, SimpMessageHeaderAccessor headerAccessor) {
        String sessionId = headerAccessor.getSessionId();
        gameService.submitWord(word, sessionId);
    }

    @MessageMapping("/game/guess")
    public void handleGuess(@Payload String guess, SimpMessageHeaderAccessor headerAccessor) {
        String sessionId = headerAccessor.getSessionId();
        gameService.processGuess(guess, sessionId);
    }

    @MessageMapping("/game/draw")
    public void handleDrawData(@Payload DrawingData data) {
        gameService.broadcastDrawing(data);
    }
}
