package com.dry_code_snippets.api.Models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ratings")
@Getter
@Setter
@NoArgsConstructor
public class SnippetTag
{
    @Column(name = "snippetId")
    private Long snippetId;

    @Column(name = "tagId")
    private Long tagId;

    public SnippetTag(Long snippetId, long tagId) {
        this.snippetId= snippetId;
        this.tagId = tagId;
    }
}