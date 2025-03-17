package com.dry_code_snippets.api.Models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "ratings")
@Getter
@Setter
@NoArgsConstructor
public class Rating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ratingId")
    private Long ratingId;

    @Column(name = "snippetId", nullable = false)
    private Long snippetId;

    @Column(name = "userId")
    private Long userId;

    @Column(name = "rating", nullable = false)
    private Integer rating;

    @Column(name = "createdAt", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public Rating(Long snippetId, Long userId, Integer rating) {
        this.snippetId = snippetId;
        this.userId = userId;
        this.rating = rating;
        this.createdAt = LocalDateTime.now();
    }
}