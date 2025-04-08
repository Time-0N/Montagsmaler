package com.example.business.service;

import com.example.model.dao.GameSession;
import com.example.model.dto.game.DrawingData;
import com.example.model.dto.game.SubmittedWord;
import com.example.model.entity.User;

public interface GameService {
    GameSession createGame(User user);

    void joinGame(String roomId, User user);

    void toggleReady(User user, String roomId);

    void submitWord(User user, String word, String roomId);

    void processGuess(User user, String guess, String roomId);

    void broadcastDrawing(DrawingData data);

    void decrementTimers();
}
