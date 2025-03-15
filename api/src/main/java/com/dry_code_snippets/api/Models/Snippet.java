package com.dry_code_snippets.api.Models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "snippets")
@Getter
@Setter
@NoArgsConstructor
public class Snippet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "snippetId")
    private Long snippetId;

    @Column(name = "userId")
    private UUID userId;

    @Column(name = "title", nullable = false, length = 256)
    private String title;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "code", nullable = false)
    private String code;

    @Column(name = "languageId", nullable = false)
    private int languageId;

    @Column(name = "updatedAt")
    private LocalDateTime updatedAt;

    @Column(name = "isDeleted")
    private Boolean isDeleted;

    @Column(name = "createdAt")
    private LocalDateTime createdAt;

    public Snippet(String userId, String title, String description, String code, int languageId) {
        this.userId = UUID.fromString(userId);
        this.title = title;
        this.description = description;
        this.code = code;
        this.languageId = languageId;
    }
}