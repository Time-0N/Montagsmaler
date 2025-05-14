package com.example.data.model.dao;

import com.example.data.model.entity.User;
import com.example.data.model.enums.GamePhase;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Data
public class GameSession {
    private String roomId;
    private List<User> users = new ArrayList<>();
    private Map<String, Boolean> readyStatus = new ConcurrentHashMap<>();
    private Map<String, String> submittedWords = new ConcurrentHashMap<>();
    private List<User> drawingOrder = new ArrayList<>();
    private int currentDrawerIndex = -1;
    private GamePhase phase = GamePhase.LOBBY;
    private String currentWord;
    private User currentDrawer;
    private int timerSeconds;

    public GameSession(String roomId) {
        this.roomId = roomId;
    }
}
