package com.leoosato.project.TokioMarine.model;

import lombok.*;
import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "users",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_users_username", columnNames = "username"),
                @UniqueConstraint(name = "uk_users_email", columnNames = "email")
        })
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserModel {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 40)
    private String username;

    @Column(nullable = false, length = 120)
    private String email;

    @Column(nullable = false, length = 100)
    private String passwordHash;   // senha criptografada

    @Column(nullable = false, length = 30)
    @Builder.Default
    private String role = "USER";  // simples por enquanto

    @Column(nullable = false)
    @Builder.Default
    private Instant createdAt = Instant.now();
}