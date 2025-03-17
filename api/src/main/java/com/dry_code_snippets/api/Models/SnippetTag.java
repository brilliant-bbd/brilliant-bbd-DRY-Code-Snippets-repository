package com.dry_code_snippets.api.Models;

import java.io.Serializable;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "SnippetTags")
@Getter
@Setter
@NoArgsConstructor
public class SnippetTag
{
    @Id
    @Column(name = "snippetTagsId")
    private Long snippetTagsId;
   
    @Column(name = "snippetId")
    private Long snippetId;

    @Column(name = "tagId")
    private Long tagId;

    public SnippetTag(Long snippetId, long tagId) {
        this.snippetId = snippetId;
        this.tagId = tagId;
    }
}