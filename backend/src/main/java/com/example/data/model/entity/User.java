package com.example.data.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Entity
@Table(name = "users")
@Data
public class User {
    @Id
    @GeneratedValue(generator = "UUID")
    @UuidGenerator
    private UUID id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(unique = true, nullable = false)
    private String email;

    private String firstName;
    private String lastName;
    private String gameWebSocketSessionId;

    @Column(name = "about_me", columnDefinition = "TEXT")
    private String aboutMe;

    @Column(nullable = false)
    private String keycloakId;
}
