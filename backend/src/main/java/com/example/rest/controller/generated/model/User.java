package com.example.rest.controller.generated.model;

import java.net.URI;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.util.UUID;
import org.springframework.lang.Nullable;
import org.openapitools.jackson.nullable.JsonNullable;
import java.time.OffsetDateTime;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import io.swagger.v3.oas.annotations.media.Schema;


import java.util.*;
import jakarta.annotation.Generated;

/**
 * User
 */

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-05-05T09:28:02.133219652+02:00[Europe/Zurich]", comments = "Generator version: 7.13.0")
public class User {

  private @Nullable UUID id;

  private @Nullable String username;

  private @Nullable String email;

  private @Nullable String firstName;

  private @Nullable String lastName;

  private @Nullable String aboutMe;

  private @Nullable String keycloakId;

  private @Nullable String gameWebSocketSessionId;

  public User id(UUID id) {
    this.id = id;
    return this;
  }

  /**
   * Get id
   * @return id
   */
  @Valid 
  @Schema(name = "id", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("id")
  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public User username(String username) {
    this.username = username;
    return this;
  }

  /**
   * Get username
   * @return username
   */
  
  @Schema(name = "username", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("username")
  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public User email(String email) {
    this.email = email;
    return this;
  }

  /**
   * Get email
   * @return email
   */
  
  @Schema(name = "email", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("email")
  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public User firstName(String firstName) {
    this.firstName = firstName;
    return this;
  }

  /**
   * Get firstName
   * @return firstName
   */
  
  @Schema(name = "firstName", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("firstName")
  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public User lastName(String lastName) {
    this.lastName = lastName;
    return this;
  }

  /**
   * Get lastName
   * @return lastName
   */
  
  @Schema(name = "lastName", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("lastName")
  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public User aboutMe(String aboutMe) {
    this.aboutMe = aboutMe;
    return this;
  }

  /**
   * Get aboutMe
   * @return aboutMe
   */
  
  @Schema(name = "aboutMe", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("aboutMe")
  public String getAboutMe() {
    return aboutMe;
  }

  public void setAboutMe(String aboutMe) {
    this.aboutMe = aboutMe;
  }

  public User keycloakId(String keycloakId) {
    this.keycloakId = keycloakId;
    return this;
  }

  /**
   * Get keycloakId
   * @return keycloakId
   */
  
  @Schema(name = "keycloakId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("keycloakId")
  public String getKeycloakId() {
    return keycloakId;
  }

  public void setKeycloakId(String keycloakId) {
    this.keycloakId = keycloakId;
  }

  public User gameWebSocketSessionId(String gameWebSocketSessionId) {
    this.gameWebSocketSessionId = gameWebSocketSessionId;
    return this;
  }

  /**
   * Get gameWebSocketSessionId
   * @return gameWebSocketSessionId
   */
  
  @Schema(name = "gameWebSocketSessionId", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
  @JsonProperty("gameWebSocketSessionId")
  public String getGameWebSocketSessionId() {
    return gameWebSocketSessionId;
  }

  public void setGameWebSocketSessionId(String gameWebSocketSessionId) {
    this.gameWebSocketSessionId = gameWebSocketSessionId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    User user = (User) o;
    return Objects.equals(this.id, user.id) &&
        Objects.equals(this.username, user.username) &&
        Objects.equals(this.email, user.email) &&
        Objects.equals(this.firstName, user.firstName) &&
        Objects.equals(this.lastName, user.lastName) &&
        Objects.equals(this.aboutMe, user.aboutMe) &&
        Objects.equals(this.keycloakId, user.keycloakId) &&
        Objects.equals(this.gameWebSocketSessionId, user.gameWebSocketSessionId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, username, email, firstName, lastName, aboutMe, keycloakId, gameWebSocketSessionId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class User {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    username: ").append(toIndentedString(username)).append("\n");
    sb.append("    email: ").append(toIndentedString(email)).append("\n");
    sb.append("    firstName: ").append(toIndentedString(firstName)).append("\n");
    sb.append("    lastName: ").append(toIndentedString(lastName)).append("\n");
    sb.append("    aboutMe: ").append(toIndentedString(aboutMe)).append("\n");
    sb.append("    keycloakId: ").append(toIndentedString(keycloakId)).append("\n");
    sb.append("    gameWebSocketSessionId: ").append(toIndentedString(gameWebSocketSessionId)).append("\n");
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

