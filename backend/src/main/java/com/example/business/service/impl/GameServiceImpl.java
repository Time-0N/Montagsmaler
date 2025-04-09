package com.example.business.service.impl;

import com.example.business.service.GameService;
import com.example.model.dao.GameSession;
import com.example.model.dto.game.ChatMessage;
import com.example.model.dto.game.DrawingData;
import com.example.model.dto.game.SubmittedWord;
import com.example.model.entity.User;
import com.example.model.enums.DefaultWord;
import com.example.model.enums.GamePhase;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GameServiceImpl implements GameService {
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int ROOM_ID_LENGTH = 6;
    private static final int MAX_ATTEMPTS = 10;
    private final SecureRandom random = new SecureRandom();

    private final Map<String, GameSession> activeGameSessions = new ConcurrentHashMap<>();
    private final SimpMessagingTemplate messagingTemplate;


    @Override
    public GameSession createGame(User user) {
        GameSession gameSession;
        do {
            String roomId = generateUniqueRoomId();
            gameSession = new GameSession(roomId);
            gameSession.getUsers().add(user);
            gameSession.getReadyStatus().put(user.getId(), false);
        } while (activeGameSessions.putIfAbsent(gameSession.getRoomId(), gameSession) != null);
        return gameSession;
    }

    @Override
    public synchronized void joinGame(String roomId, User user) {
        GameSession gameSession = activeGameSessions.get(roomId);
        if (gameSession != null && gameSession.getUsers().size() < 15) {
            gameSession.getUsers().add(user);
            gameSession.getReadyStatus().put(user.getId(), false);
        }
    }

    @Override
    public synchronized void leaveGame(User user, String roomId) {
        GameSession session = activeGameSessions.get(roomId);
        if (session != null) {
            session.getUsers().removeIf(u -> u.getId().equals(user.getId()));
            session.getReadyStatus().remove(user.getId());
            session.getSubmittedWords().remove(user.getId());

            if (session.getUsers().isEmpty()) {
                activeGameSessions.remove(roomId);
            } else {
                broadcastGameState(roomId);
            }
        }
    }

    @Override
    public synchronized void toggleReady(User user, String roomId) {
        GameSession session = activeGameSessions.get(roomId);
        if (session == null || session.getPhase() != GamePhase.LOBBY) return;

        boolean newStatus = !session.getReadyStatus().getOrDefault(user.getId(), false);
        session.getReadyStatus().put(user.getId(), newStatus);

        broadcastGameState(roomId);
        checkStartConditions(session);
    }

    @Override
    public synchronized void submitWord(User user, String word, String roomId) {
        GameSession gameSession = activeGameSessions.get(roomId);
        if (gameSession != null && gameSession.getPhase() == GamePhase.WORD_SUBMISSION) {
            gameSession.getSubmittedWords().put(user.getId(), word);
            if (gameSession.getSubmittedWords().size() == gameSession.getUsers().size()) {
                startDrawingPhase(gameSession);
            }
        }
    }

    @Override
    public void processGuess(User user, String guess, String roomId, String sessionId) {
        GameSession gameSession = activeGameSessions.get(roomId);
        if (gameSession == null || gameSession.getPhase() != GamePhase.DRAWING) return;

        User currentDrawingUser = gameSession.getDrawingOrder().get(gameSession.getCurrentDrawerIndex());
        UUID wordAuthorId = gameSession.getSubmittedWords().entrySet().stream()
                .filter(e -> e.getValue().equalsIgnoreCase(gameSession.getCurrentWord()))
                .findFirst()
                .map(Map.Entry::getKey)
                .orElse(null);

        if (user.getId().equals(currentDrawingUser.getId()) || user.getId().equals(wordAuthorId)) {
            sendPrivateMessage(user.getId(), "System: You can't guess int this round!");
            return;
        }

        ChatMessage chatMessage = new ChatMessage(user.getUsername(), guess);

        if (guess.equalsIgnoreCase(gameSession.getCurrentWord())) {
            chatMessage.setCorrectGuess(true);
            handleCorrectGuess(gameSession, user);
        }

        messagingTemplate.convertAndSend(
                "/topic/game/" + roomId + "/chat",
                chatMessage,
                createExcludeSenderHeaders(sessionId)
        );
    }

    @Scheduled(fixedRate = 1000)
    @Override
    public void decrementTimers() {
        activeGameSessions.forEach((roomId, gameSession) -> {
            if (gameSession.getTimerSeconds() > 0) {
                gameSession.setTimerSeconds(gameSession.getTimerSeconds() - 1);

                if (gameSession.getTimerSeconds() == 0) {
                    handlePhaseTransition(gameSession);
                }

                broadcastGameState(roomId);
            }
        });
    }

    @Override
    public void broadcastDrawing(DrawingData data) {
        if (!activeGameSessions.containsKey(data.getRoomId())) {
            throw new IllegalArgumentException("Invalid room ID: " + data.getRoomId());
        }

        String destination = "/topic/game/" + data.getRoomId() + "/draw";

        messagingTemplate.convertAndSend(
                destination,
                data,
                createExcludeSenderHeaders(data.getSenderSessionId())
        );
    }

    private String generateUniqueRoomId() {
        for (int i = 0; i < MAX_ATTEMPTS; i++) {
            String candidate = generateRandomString();
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
                && gameSession.getReadyStatus().keySet().containsAll(
                        gameSession.getUsers().stream()
                                .map(User::getId)
                                .collect(Collectors.toSet())
        )) {
            gameSession.setPhase(GamePhase.WORD_SUBMISSION);
            gameSession.setTimerSeconds(20);
            broadcastGameState(gameSession.getRoomId());
        }
    }

    private void startDrawingPhase(GameSession gameSession) {
        List<User> players = new ArrayList<>(gameSession.getUsers());
        Collections.shuffle(players);

        gameSession.setDrawingOrder(players);
        gameSession.setCurrentDrawerIndex(0);

        List<String> eligibleWords = gameSession.getSubmittedWords().entrySet().stream()
                .filter(e -> !e.getKey().equals(players.get(0).getId()))
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());

        if (eligibleWords.isEmpty()) {
            eligibleWords.add(getRandomDefaultWord());
        }

        gameSession.setCurrentWord(eligibleWords.get(random.nextInt(eligibleWords.size())));
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

    private void handlePhaseTransition(GameSession gameSession) {
        switch (gameSession.getPhase()) {
            case WORD_SUBMISSION:
                gameSession.getUsers().forEach(user ->
                        gameSession.getSubmittedWords().computeIfAbsent(user.getId(),
                                k -> getRandomDefaultWord())
                        );
                startDrawingPhase(gameSession);
                break;

            case DRAWING:
                if (gameSession.getCurrentDrawerIndex() < gameSession.getDrawingOrder().size() -1 ) {
                    gameSession.setCurrentDrawerIndex(gameSession.getCurrentDrawerIndex() + 1);
                    gameSession.setCurrentWord(getAssignedWord(
                            gameSession.getDrawingOrder().get(gameSession.getCurrentDrawerIndex()),
                            gameSession
                    ));
                    gameSession.setTimerSeconds(100);
                } else {
                    gameSession.setPhase(GamePhase.RESULTS);
                    gameSession.setTimerSeconds(15);
                }
                break;

            case RESULTS:
                resetGameSession(gameSession);
                broadcastGameState(gameSession.getRoomId());
                break;
        }
    }

    private void resetGameSession(GameSession gameSession) {
        gameSession.setPhase(GamePhase.LOBBY);
        gameSession.getReadyStatus().replaceAll((k, v) -> false);
        gameSession.setSubmittedWords(new ConcurrentHashMap<>());
        gameSession.setDrawingOrder(new ArrayList<>());
        gameSession.setCurrentDrawerIndex(-1);
        gameSession.setCurrentWord(null);

        if (gameSession.getUsers().isEmpty()) {
            activeGameSessions.remove(gameSession.getRoomId());
        }
    }

    @Scheduled(fixedRate = 300_000)
    public void cleanInactiveSessions() {
        Instant cutoff = Instant.now().minus(2, ChronoUnit.HOURS);
        activeGameSessions.entrySet().removeIf(entry ->
                entry.getValue().getUsers().isEmpty()
        );
    }

    private void broadcastGameState(String roomId) {
        messagingTemplate.convertAndSend(
                "/topic/game/" + roomId + "/state",
                activeGameSessions.get(roomId)
        );
    }

    private Map<String, Object> createExcludeSenderHeaders(String sessionId) {
        Map<String, Object> headers = new HashMap<>();
        headers.put("simpSessionId", sessionId);
        return headers;
    }

    private String getRandomDefaultWord() {
        DefaultWord[] words = DefaultWord.values();
        return words[random.nextInt(words.length)].name();
    }

    private void handleCorrectGuess(GameSession gameSession, User guesser) {
        synchronized (gameSession) {
            if (gameSession.getPhase() != GamePhase.DRAWING) return;

            gameSession.setTimerSeconds(0);

            broadcastGameState(gameSession.getRoomId());
        }

        ChatMessage systemMsg = new ChatMessage("System",
                guesser.getUsername() + " guessed correctly!");
        messagingTemplate.convertAndSend(
                "/topic/game/" + gameSession.getRoomId() + "/chat",
                systemMsg
        );
    }

    private void sendPrivateMessage(UUID userId, String message) {
        String userIdString = userId.toString();
        messagingTemplate.convertAndSendToUser(
                userIdString,
                "/queue/notifications",
                new ChatMessage("System ", message)
        );
    }
}
