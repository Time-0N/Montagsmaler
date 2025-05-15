package com.example.rest.generated.model;

import java.net.URI;
import java.util.Objects;
import com.example.rest.generated.model.GamePhase;
import com.example.rest.generated.model.User;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
@lombok.Data @lombok.AllArgsConstructor @lombok.Builder

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-05-16T01:03:39.342360425+02:00[Europe/Zurich]", comments = "Generator version: 7.7.0")
public class GameSession {

  private String roomId;

  @Valid
  private List<@Valid User> users = new ArrayList<>();

  @Valid
  private Map<String, Boolean> readyStatus = new HashMap<>();

  @Valid
  private Map<String, String> submittedWords = new HashMap<>();

  @Valid
  private List<@Valid User> drawingOrder = new ArrayList<>();

  private Integer currentDrawerIndex;

  private GamePhase phase;

  private String currentWord;

  private User currentDrawer;

  private Integer timerSeconds;

}

