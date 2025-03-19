package com.dry_code_snippets.api.Models;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SnippetTagTest {

    @Test
    public void testSnippetTagCreation() {
        SnippetTag snippetTag = new SnippetTag(1L, 2L);

        assertNotNull(snippetTag);
        assertNotNull(snippetTag.getId());
    }
}
