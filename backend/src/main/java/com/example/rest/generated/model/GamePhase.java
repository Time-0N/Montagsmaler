package com.example.rest.generated.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonValue;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Gets or Sets GamePhase
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-05-14T23:51:54.044638867+02:00[Europe/Zurich]", comments = "Generator version: 7.7.0")
public enum GamePhase {
  
  LOBBY("LOBBY"),
  
  WORD_SUBMISSION("WORD_SUBMISSION"),
  
  DRAWING("DRAWING"),
  
  RESULTS("RESULTS");

  private String value;

  GamePhase(String value) {
    this.value = value;
  }

  @JsonValue
  public String getValue() {
    return value;
  }

  @Override
  public String toString() {
    return String.valueOf(value);
  }

  @JsonCreator
  public static GamePhase fromValue(String value) {
    for (GamePhase b : GamePhase.values()) {
      if (b.value.equals(value)) {
        return b;
      }
    }
    throw new IllegalArgumentException("Unexpected value '" + value + "'");
  }
}

