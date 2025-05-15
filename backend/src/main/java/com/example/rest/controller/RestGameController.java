package com.example.rest.controller;

import com.example.mappers.GameSessionMapper;
import com.example.rest.generated.model.GameSession;
import com.example.security.util.CurrentUserProvider;
import com.example.service.GameService;
import com.example.data.model.entity.User;
import com.example.rest.generated.GameApi;
import com.example.security.annotation.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/game")
@RequiredArgsConstructor
public class RestGameController implements GameApi {
    private final GameService gameService;
    private final CurrentUserProvider currentUserProvider;

    @Override
    @PostMapping("/create")
    public ResponseEntity<GameSession> createGame() {
        com.example.data.model.entity.User currentUser = currentUserProvider.getCurrentUser();
        return ResponseEntity.ok(GameSessionMapper.toRest(gameService.createGame(currentUser)));
    }

    @Override
    @PostMapping("/{roomId}/join")
    public ResponseEntity<GameSession> joinGame(@PathVariable String roomId) {
        com.example.data.model.entity.User currentUser = currentUserProvider.getCurrentUser();
        return ResponseEntity.ok(GameSessionMapper.toRest(gameService.joinGame(roomId, currentUser)));
    }

    @Override
    @PostMapping("/{roomId}/leave-game")
    public ResponseEntity<Void> leaveGame(@PathVariable String roomId) {
        com.example.data.model.entity.User currentUser = currentUserProvider.getCurrentUser();
        gameService.leaveGame(currentUser, roomId);
        return ResponseEntity.noContent().build();
    }

    @Override
    @PostMapping("/{roomId}/submit-word")
    public ResponseEntity<Void> submitWord(@PathVariable String roomId, @RequestBody String word) {
        com.example.data.model.entity.User currentUser = currentUserProvider.getCurrentUser();
        gameService.submitWord(currentUser, word, roomId);
        return ResponseEntity.noContent().build();
    }

    @Override
    @PostMapping("/{roomId}/ready")
    public ResponseEntity<Void> toggleReady(String roomId) {
        com.example.data.model.entity.User currentUser = currentUserProvider.getCurrentUser();
        gameService.toggleReady(currentUser, roomId);
        return ResponseEntity.ok().build();
    }
}
