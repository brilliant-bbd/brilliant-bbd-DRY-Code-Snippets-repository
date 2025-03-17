package com.dry_code_snippets.api.Models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "comments")
@Getter
@Setter
@NoArgsConstructor
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "commentId")
    private Long commentId;

    @Column(name = "snippetId", nullable = false)
    private Long snippetId;

    @Column(name = "userId")
    private Long userId;

    @Column(name = "comment", nullable = false)
    private String comment;

    @Column(name = "createdAt")
    private LocalDateTime createdAt;

    public Comment(Long snippetId, Long userId, String comment) {
        this.snippetId = snippetId;
        this.userId = userId;
        this.comment = comment;
        this.createdAt = LocalDateTime.now();
    }
}