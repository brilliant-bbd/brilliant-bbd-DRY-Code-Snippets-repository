package com.dry_code_snippets.api.Models;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "userId")
    private Long userId;

    @Column(name = "userGuid")
    private UUID userGuid;

    @Column(name = "createdAt")
    private LocalDateTime createdAt;

    public User(UUID userGuid)
    {
        this.userGuid = userGuid;
        this.createdAt = LocalDateTime.now();
    }
}
