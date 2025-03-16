package com.dry_code_snippets.DTO;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class SnippetDTO {

    private Long snippetId;
    private Long userId;
    private String title;
    private String description;
    private String language;
    private String code;
    private LocalDateTime updatedAt;

    public SnippetDTO(Long snippetId, Long userId, String title, String description, String language, LocalDateTime updatedAt, String code) {
        this.snippetId = snippetId;
        this.userId = userId;
        this.title = title;
        this.description = description;
        this.language = language;
        this.updatedAt = updatedAt;
        this.code = code;
    }
}
