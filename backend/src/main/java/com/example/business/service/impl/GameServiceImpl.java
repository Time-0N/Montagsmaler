package com.example.business.service.impl;

import com.example.business.service.GameService;
import com.example.model.dao.GameSession;
import com.example.model.dto.game.DrawingData;
import com.example.model.dto.game.SubmittedWord;
import com.example.model.entity.User;
import com.example.model.enums.GamePhase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class GameServiceImpl implements GameService {
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int ROOM_ID_LENGTH = 6;
    private static final int MAX_ATTEMPTS = 10;
    private final SecureRandom random = new SecureRandom();

    private final Map<String, GameSession> activeGameSessions = new ConcurrentHashMap<>();
    private final SimpleMessagingTemplate messagingTemplate;


    @Override
    public GameSession createGame(User user) {
        GameSession gameSession;

        do {
            String roomId = generateUniqueRoomId();
            gameSession = new GameSession(roomId);
        } while (activeGameSessions.putIfAbsent(gameSession.getRoomId(), gameSession) != null);

        return gameSession;
    }

    @Override
    public synchronized void joinGame(String roomId, User user) {
        GameSession gameSession = activeGameSessions.get(roomId);
        if (gameSession != null && gameSession.getUsers().size() < 15) {
            gameSession.getUsers().add(user);
            gameSession.getReadyStatus().put(user.getUsername(), false);
        }
    }

    @Override
    public synchronized void toggleReady(User user, String roomId) {
        GameSession gameSession = activeGameSessions.get(roomId);
        if (gameSession != null && gameSession.getPhase() == GamePhase.LOBBY) {
            boolean newStatus = !gameSession.getReadyStatus().getOrDefault(user.getUsername(), false);
            gameSession.getReadyStatus().put(user.getUsername(), newStatus);

            checkStartConditions(gameSession);
        }
    }

    @Override
    public synchronized void submitWord(User user, String word, String roomId) {
        GameSession gameSession = activeGameSessions.get(roomId);
        if (gameSession != null && gameSession.getPhase() == GamePhase.LOBBY) {
            gameSession.getSubmittedWords().put(user.getId(), word);
            if (gameSession.getSubmittedWords().size() == gameSession.getUsers().size()) {
                startDrawingPhase(gameSession);
            }
        }
    }

    @Override
    public void processGuess(User user, String guess, String roomId) {
        GameSession gameSession = activeGameSessions.get(roomId);
        if (gameSession != null && gameSession.getPhase() == GamePhase.DRAWING) {
            User currentDrawingUser = gameSession.getDrawingOrder().get(gameSession.getCurrentDrawerIndex());
            if (!user.getId().equals(currentDrawingUser.getId())
                    && !user.getId().equals(gameSession)
            ) {}
        }
    }

    @Override
    public void broadcastDrawing(DrawingData data) {

    }

    @Override
    public void decrementTimers() {

    }

    private String generateUniqueRoomId() {
        for (int i = 0; i < MAX_ATTEMPTS; i++) {
            String candidate = generateUniqueRoomId();
            if (!activeGameSessions.containsKey(candidate)) {
                return candidate;
            }
        }
        throw new IllegalStateException("Failed to generate unique room ID after" + MAX_ATTEMPTS + " attempts");
    }

    private String generateRandomString() {
        return random.ints(ROOM_ID_LENGTH, 0, CHARACTERS.length())
                .mapToObj(CHARACTERS::charAt)
                .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
                .toString();
    }

    private void checkStartConditions(GameSession gameSession) {
        if (gameSession.getPhase() == GamePhase.LOBBY
                && gameSession.getUsers().size() >= 3
                && gameSession.getReadyStatus().values().stream().allMatch(Boolean::valueOf)) {
            gameSession.setPhase(GamePhase.WORD_SUBMISSION);
            gameSession.setTimerSeconds(20);
            broadcastDrawing(gameSession.getRoomId());
        }
    }

    private void startDrawingPhase(GameSession gameSession) {
        List<User> users = new ArrayList<>(gameSession.getUsers());
        Collections.shuffle(users);
        gameSession.setDrawingOrder(users);
        gameSession.setCurrentDrawerIndex(0);

        User firstDrawingUser = users.get(0);
        gameSession.setCurrentWord(getAssignedWord(firstDrawingUser, gameSession));
        gameSession.setPhase(GamePhase.DRAWING);
        gameSession.setTimerSeconds(100);

        broadcastGameState(gameSession.getRoomId());
    }

    private String getAssignedWord(User user, GameSession gameSession) {
        return gameSession.getSubmittedWords().entrySet().stream()
                .filter(e -> !e.getKey().equals(user.getId()))
                .findAny()
                .map(Map.Entry::getValue)
                .orElseGet(() -> "DEFAULT_WORD");
    }
}
