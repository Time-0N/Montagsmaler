package com.example.rest.controller.generated.model;

import java.net.URI;
import java.util.Objects;
import com.example.rest.controller.generated.model.User;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * GameSession
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-05-05T09:28:02.133219652+02:00[Europe/Zurich]", comments = "Generator version: 7.13.0")
public class GameSession {

  private @Nullable String roomId;

  @Valid
  private List<@Valid User> users = new ArrayList<>();

  @Valid
  private Map<String, Boolean> readyStatus = new HashMap<>();

  @Valid
  private Map<String, String> submittedWords = new HashMap<>();

  @Valid
  private List<@Valid User> drawingOrder = new ArrayList<>();

  private @Nullable Integer currentDrawerIndex;

  private @Nullable String phase;

  private @Nullable String currentWord;

  private @Nullable User currentDrawer;

  private @Nullable Integer timerSeconds;

  public GameSession roomId(String roomId) {
    this.roomId = roomId;
    return this;
  }

  /**
   * Get roomId
   * @return roomId
   */
  
  @Schema(name = "roomId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("roomId")
  public String getRoomId() {
    return roomId;
  }

  public void setRoomId(String roomId) {
    this.roomId = roomId;
  }

  public GameSession users(List<@Valid User> users) {
    this.users = users;
    return this;
  }

  public GameSession addUsersItem(User usersItem) {
    if (this.users == null) {
      this.users = new ArrayList<>();
    }
    this.users.add(usersItem);
    return this;
  }

  /**
   * Get users
   * @return users
   */
  @Valid 
  @Schema(name = "users", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("users")
  public List<@Valid User> getUsers() {
    return users;
  }

  public void setUsers(List<@Valid User> users) {
    this.users = users;
  }

  public GameSession readyStatus(Map<String, Boolean> readyStatus) {
    this.readyStatus = readyStatus;
    return this;
  }

  public GameSession putReadyStatusItem(String key, Boolean readyStatusItem) {
    if (this.readyStatus == null) {
      this.readyStatus = new HashMap<>();
    }
    this.readyStatus.put(key, readyStatusItem);
    return this;
  }

  /**
   * Get readyStatus
   * @return readyStatus
   */
  
  @Schema(name = "readyStatus", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("readyStatus")
  public Map<String, Boolean> getReadyStatus() {
    return readyStatus;
  }

  public void setReadyStatus(Map<String, Boolean> readyStatus) {
    this.readyStatus = readyStatus;
  }

  public GameSession submittedWords(Map<String, String> submittedWords) {
    this.submittedWords = submittedWords;
    return this;
  }

  public GameSession putSubmittedWordsItem(String key, String submittedWordsItem) {
    if (this.submittedWords == null) {
      this.submittedWords = new HashMap<>();
    }
    this.submittedWords.put(key, submittedWordsItem);
    return this;
  }

  /**
   * Get submittedWords
   * @return submittedWords
   */
  
  @Schema(name = "submittedWords", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("submittedWords")
  public Map<String, String> getSubmittedWords() {
    return submittedWords;
  }

  public void setSubmittedWords(Map<String, String> submittedWords) {
    this.submittedWords = submittedWords;
  }

  public GameSession drawingOrder(List<@Valid User> drawingOrder) {
    this.drawingOrder = drawingOrder;
    return this;
  }

  public GameSession addDrawingOrderItem(User drawingOrderItem) {
    if (this.drawingOrder == null) {
      this.drawingOrder = new ArrayList<>();
    }
    this.drawingOrder.add(drawingOrderItem);
    return this;
  }

  /**
   * Get drawingOrder
   * @return drawingOrder
   */
  @Valid 
  @Schema(name = "drawingOrder", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("drawingOrder")
  public List<@Valid User> getDrawingOrder() {
    return drawingOrder;
  }

  public void setDrawingOrder(List<@Valid User> drawingOrder) {
    this.drawingOrder = drawingOrder;
  }

  public GameSession currentDrawerIndex(Integer currentDrawerIndex) {
    this.currentDrawerIndex = currentDrawerIndex;
    return this;
  }

  /**
   * Get currentDrawerIndex
   * @return currentDrawerIndex
   */
  
  @Schema(name = "currentDrawerIndex", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("currentDrawerIndex")
  public Integer getCurrentDrawerIndex() {
    return currentDrawerIndex;
  }

  public void setCurrentDrawerIndex(Integer currentDrawerIndex) {
    this.currentDrawerIndex = currentDrawerIndex;
  }

  public GameSession phase(String phase) {
    this.phase = phase;
    return this;
  }

  /**
   * Get phase
   * @return phase
   */
  
  @Schema(name = "phase", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("phase")
  public String getPhase() {
    return phase;
  }

  public void setPhase(String phase) {
    this.phase = phase;
  }

  public GameSession currentWord(String currentWord) {
    this.currentWord = currentWord;
    return this;
  }

  /**
   * Get currentWord
   * @return currentWord
   */
  
  @Schema(name = "currentWord", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("currentWord")
  public String getCurrentWord() {
    return currentWord;
  }

  public void setCurrentWord(String currentWord) {
    this.currentWord = currentWord;
  }

  public GameSession currentDrawer(User currentDrawer) {
    this.currentDrawer = currentDrawer;
    return this;
  }

  /**
   * Get currentDrawer
   * @return currentDrawer
   */
  @Valid 
  @Schema(name = "currentDrawer", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("currentDrawer")
  public User getCurrentDrawer() {
    return currentDrawer;
  }

  public void setCurrentDrawer(User currentDrawer) {
    this.currentDrawer = currentDrawer;
  }

  public GameSession timerSeconds(Integer timerSeconds) {
    this.timerSeconds = timerSeconds;
    return this;
  }

  /**
   * Get timerSeconds
   * @return timerSeconds
   */
  
  @Schema(name = "timerSeconds", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("timerSeconds")
  public Integer getTimerSeconds() {
    return timerSeconds;
  }

  public void setTimerSeconds(Integer timerSeconds) {
    this.timerSeconds = timerSeconds;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GameSession gameSession = (GameSession) o;
    return Objects.equals(this.roomId, gameSession.roomId) &&
        Objects.equals(this.users, gameSession.users) &&
        Objects.equals(this.readyStatus, gameSession.readyStatus) &&
        Objects.equals(this.submittedWords, gameSession.submittedWords) &&
        Objects.equals(this.drawingOrder, gameSession.drawingOrder) &&
        Objects.equals(this.currentDrawerIndex, gameSession.currentDrawerIndex) &&
        Objects.equals(this.phase, gameSession.phase) &&
        Objects.equals(this.currentWord, gameSession.currentWord) &&
        Objects.equals(this.currentDrawer, gameSession.currentDrawer) &&
        Objects.equals(this.timerSeconds, gameSession.timerSeconds);
  }

  @Override
  public int hashCode() {
    return Objects.hash(roomId, users, readyStatus, submittedWords, drawingOrder, currentDrawerIndex, phase, currentWord, currentDrawer, timerSeconds);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GameSession {\n");
    sb.append("    roomId: ").append(toIndentedString(roomId)).append("\n");
    sb.append("    users: ").append(toIndentedString(users)).append("\n");
    sb.append("    readyStatus: ").append(toIndentedString(readyStatus)).append("\n");
    sb.append("    submittedWords: ").append(toIndentedString(submittedWords)).append("\n");
    sb.append("    drawingOrder: ").append(toIndentedString(drawingOrder)).append("\n");
    sb.append("    currentDrawerIndex: ").append(toIndentedString(currentDrawerIndex)).append("\n");
    sb.append("    phase: ").append(toIndentedString(phase)).append("\n");
    sb.append("    currentWord: ").append(toIndentedString(currentWord)).append("\n");
    sb.append("    currentDrawer: ").append(toIndentedString(currentDrawer)).append("\n");
    sb.append("    timerSeconds: ").append(toIndentedString(timerSeconds)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

