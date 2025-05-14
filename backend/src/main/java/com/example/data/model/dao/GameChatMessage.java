package com.example.data.model.dao;

import lombok.Data;

import java.time.Instant;

@Data
public class GameChatMessage {
    private String username;
    private String content;
    private boolean isCorrectGuess;
    private Instant timestamp;

    public GameChatMessage(String username, String content) {
        this.username = username;
        this.timestamp = Instant.now();
        this.isCorrectGuess = false;
        this.content = content;
    }
}
