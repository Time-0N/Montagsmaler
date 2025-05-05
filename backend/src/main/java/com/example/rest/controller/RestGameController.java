package com.example.rest.controller;

import com.example.business.service.GameService;
import com.example.model.dao.GameSession;
import com.example.model.entity.User;
import com.example.rest.controller.generated.GameApi;
import com.example.security.annotation.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/game")
@RequiredArgsConstructor
public class RestGameController implements GameApi {
    private final GameService gameService;

    @PostMapping("/create")
    public ResponseEntity<GameSession> createGame(
            @CurrentUser User user
    ) {
        return ResponseEntity.ok(gameService.createGame(user));
    }

    @PostMapping("/{roomId}/join")
    public ResponseEntity<Void> joinGame(
            @CurrentUser User user,
            @PathVariable String roomId
    ) {
        gameService.joinGame(roomId, user);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{roomId}/ready")
    public ResponseEntity<Void> toggleReady(
            @CurrentUser User user,
            @PathVariable String roomId
    ) {
        gameService.toggleReady(user, roomId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{roomId}/submit-word")
    public ResponseEntity<Void> submitWord(
            @CurrentUser User user,
            @PathVariable String roomId,
            @RequestBody String word
    ) {
        gameService.submitWord(user, word, roomId);
        return ResponseEntity.ok().build();
    }
}
