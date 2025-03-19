package com.dry_code_snippets.api.Models;

import org.junit.jupiter.api.Test;

import com.dry_code_snippets.api.Models.Snippet;

import static org.junit.jupiter.api.Assertions.*;

public class SnippetTest {

    @Test
    public void testSnippetRecordCreation() {
        Snippet snippet = new Snippet(1L, "Test Snippet", "This is a test description", 1L);

        assertNotNull(snippet);
        assertEquals(1L, snippet.getUserId());
        assertEquals("Test Snippet", snippet.getTitle());
        assertEquals("This is a test description", snippet.getDescription());
        assertEquals(1, snippet.getLanguageId());
    }
}