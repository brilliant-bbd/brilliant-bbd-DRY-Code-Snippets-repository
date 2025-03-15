package com.dry_code_snippets.api.Snippets;

import org.junit.jupiter.api.Test;

import com.dry_code_snippets.api.Models.Snippet;

import static org.junit.jupiter.api.Assertions.*;

public class SnippetTest {

    @Test
    public void testSnippetRecordCreation() {
        Snippet snippet = new Snippet("0000001000000212220021201", "Test Snippet", "This is a test description", "System.out.println(\"Hello World\");", 1);

        assertNotNull(snippet);
        assertEquals("userId", snippet.getUserId());
        assertEquals("Test Snippet", snippet.getTitle());
        assertEquals("This is a test description", snippet.getDescription());
        assertEquals("System.out.println(\"Hello World\");", snippet.getCode());
        assertEquals(1, snippet.getLanguageId());
    }
}