package com.dry_code_snippets.api.Models;

import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import java.io.Serializable;

@Embeddable
@EqualsAndHashCode
public class SnippetTagId implements Serializable {
    private Long snippetId;
    private Long tagId;

    public SnippetTagId() {}

    public SnippetTagId(Long snippetId, Long tagId) {
        this.snippetId = snippetId;
        this.tagId = tagId;
    }
}
 