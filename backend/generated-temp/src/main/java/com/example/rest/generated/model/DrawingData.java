package com.example.rest.generated.model;

import java.net.URI;
import java.util.Objects;
import com.example.rest.generated.model.DrawingDataFrom;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import org.springframework.lang.Nullable;
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

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-05-09T14:45:29.811620744+02:00[Europe/Zurich]", comments = "Generator version: 7.13.0")
public class DrawingData {

  private String roomId;

  private String senderSessionId;

  private String color;

  private Float width;

  private DrawingDataFrom from;

  private DrawingDataFrom to;

  public DrawingData() {
    super();
  }

  /**
   * Constructor with only required parameters
   */
  public DrawingData(String roomId, String senderSessionId, String color, Float width, DrawingDataFrom from, DrawingDataFrom to) {
    this.roomId = roomId;
    this.senderSessionId = senderSessionId;
    this.color = color;
    this.width = width;
    this.from = from;
    this.to = to;
  }

  public DrawingData roomId(String roomId) {
    this.roomId = roomId;
    return this;
  }

  /**
   * Get roomId
   * @return roomId
   */
  @NotNull 
  @Schema(name = "roomId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("roomId")
  public String getRoomId() {
    return roomId;
  }

  public void setRoomId(String roomId) {
    this.roomId = roomId;
  }

  public DrawingData senderSessionId(String senderSessionId) {
    this.senderSessionId = senderSessionId;
    return this;
  }

  /**
   * Get senderSessionId
   * @return senderSessionId
   */
  @NotNull 
  @Schema(name = "senderSessionId", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("senderSessionId")
  public String getSenderSessionId() {
    return senderSessionId;
  }

  public void setSenderSessionId(String senderSessionId) {
    this.senderSessionId = senderSessionId;
  }

  public DrawingData color(String color) {
    this.color = color;
    return this;
  }

  /**
   * Get color
   * @return color
   */
  @NotNull 
  @Schema(name = "color", example = "#000000", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("color")
  public String getColor() {
    return color;
  }

  public void setColor(String color) {
    this.color = color;
  }

  public DrawingData width(Float width) {
    this.width = width;
    return this;
  }

  /**
   * Get width
   * @return width
   */
  @NotNull 
  @Schema(name = "width", example = "2", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("width")
  public Float getWidth() {
    return width;
  }

  public void setWidth(Float width) {
    this.width = width;
  }

  public DrawingData from(DrawingDataFrom from) {
    this.from = from;
    return this;
  }

  /**
   * Get from
   * @return from
   */
  @NotNull @Valid 
  @Schema(name = "from", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("from")
  public DrawingDataFrom getFrom() {
    return from;
  }

  public void setFrom(DrawingDataFrom from) {
    this.from = from;
  }

  public DrawingData to(DrawingDataFrom to) {
    this.to = to;
    return this;
  }

  /**
   * Get to
   * @return to
   */
  @NotNull @Valid 
  @Schema(name = "to", requiredMode = Schema.RequiredMode.REQUIRED)
  @JsonProperty("to")
  public DrawingDataFrom getTo() {
    return to;
  }

  public void setTo(DrawingDataFrom to) {
    this.to = to;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DrawingData drawingData = (DrawingData) o;
    return Objects.equals(this.roomId, drawingData.roomId) &&
        Objects.equals(this.senderSessionId, drawingData.senderSessionId) &&
        Objects.equals(this.color, drawingData.color) &&
        Objects.equals(this.width, drawingData.width) &&
        Objects.equals(this.from, drawingData.from) &&
        Objects.equals(this.to, drawingData.to);
  }

  @Override
  public int hashCode() {
    return Objects.hash(roomId, senderSessionId, color, width, from, to);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DrawingData {\n");
    sb.append("    roomId: ").append(toIndentedString(roomId)).append("\n");
    sb.append("    senderSessionId: ").append(toIndentedString(senderSessionId)).append("\n");
    sb.append("    color: ").append(toIndentedString(color)).append("\n");
    sb.append("    width: ").append(toIndentedString(width)).append("\n");
    sb.append("    from: ").append(toIndentedString(from)).append("\n");
    sb.append("    to: ").append(toIndentedString(to)).append("\n");
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

