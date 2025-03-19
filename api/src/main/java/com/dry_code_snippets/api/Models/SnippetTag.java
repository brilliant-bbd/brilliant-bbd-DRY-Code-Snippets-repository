package com.dry_code_snippets.api.Models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Snippettags")
@Getter
@Setter
@NoArgsConstructor
public class SnippetTag
{
    @EmbeddedId
    private SnippetTagId id;

    public SnippetTag(Long snippetId, Long tagId) {
        this.id = new SnippetTagId(snippetId, tagId);
    }
}