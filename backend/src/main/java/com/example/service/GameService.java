package com.example.service;

import com.example.data.model.dao.GameSession;
import com.example.data.model.entity.User;
import com.example.rest.generated.model.DrawingData;

public interface GameService {
    GameSession createGame(User user);

    GameSession joinGame(String roomId, User user);

    void leaveGame(User user, String roomId);

    void toggleReady(User user, String roomId);

    void submitWord(User user, String word, String roomId);

    void processGuess(User user, String guess, String roomId, String sessionId);

    void broadcastDrawing(DrawingData data);

    void decrementTimers();
}
