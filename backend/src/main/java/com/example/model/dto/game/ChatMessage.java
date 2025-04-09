package com.example.model.dto.game;

import lombok.Data;

import java.time.Instant;

@Data
public class ChatMessage {
    private String username;
    private String content;
    private boolean isCorrectGuess;
    private Instant timestamp;

    public ChatMessage(String username, String content) {
        this.username = username;
        this.timestamp = Instant.now();
        this.isCorrectGuess = false;
        this.content = content;
    }
}
