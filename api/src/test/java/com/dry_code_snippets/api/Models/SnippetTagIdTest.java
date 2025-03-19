package com.dry_code_snippets.api.Models;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SnippetTagIdTest {

    @Test
    public void testEqualsAndHashCode() {
        SnippetTagId id1 = new SnippetTagId(1L, 2L);
        SnippetTagId id2 = new SnippetTagId(1L, 2L);
        SnippetTagId id3 = new SnippetTagId(3L, 4L);

        assertEquals(id1, id2);  
        assertNotEquals(id1, id3); 
        assertEquals(id1.hashCode(), id2.hashCode()); 
        assertNotEquals(id1.hashCode(), id3.hashCode()); 
    }

    @Test
    public void testDefaultConstructor() {
        SnippetTagId snippetTagId = new SnippetTagId();
        assertNotNull(snippetTagId);
    }
}
