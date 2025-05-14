package com.example.rest.generated.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.HashMap;
import java.util.Map;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * DrawingData
 */
@lombok.Data @lombok.AllArgsConstructor @lombok.Builder

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-05-14T23:51:54.044638867+02:00[Europe/Zurich]", comments = "Generator version: 7.7.0")
public class DrawingData {

  private String roomId;

  @Valid
  private Map<String, Object> path = new HashMap<>();

  private String senderSessionId;

  public DrawingData() {
    super();
  }

}

