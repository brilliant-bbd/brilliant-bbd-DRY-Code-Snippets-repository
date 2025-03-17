package com.dry_code_snippets.api.Models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

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
    private Long userId;

    @Column(name = "title", nullable = false, length = 256)
    private String title;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "languageId", nullable = false)
    private int languageId;

    @Column(name = "isDeleted")
    private boolean isDeleted;

    public Snippet(Long userId, String title, String description, int languageId) {
        this.userId = userId; 
        this.title = title;
        this.description = description;
        this.languageId = languageId;
    }
}