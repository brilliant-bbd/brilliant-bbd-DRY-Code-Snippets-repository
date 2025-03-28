package com.dry_code_snippets.api.DTO;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class SnippetDTO {

    private Long snippetId;
    private Long userId;
    private String title;
    private String description;
    private String language;
    private String code;
    private String updatedat;
    private BigDecimal rating;
    private String[] tags;

    public SnippetDTO(Long snippetId, Long userId, String title, String description, String language, String updatedAt, String code,BigDecimal rating, String[] tags) {
        this.snippetId = snippetId;
        this.userId = userId;
        this.title = title;
        this.description = description;
        this.language = language;
        this.updatedat = updatedAt;
        this.code = code;
        this.rating =rating;
        this.tags = tags;
    }
}
