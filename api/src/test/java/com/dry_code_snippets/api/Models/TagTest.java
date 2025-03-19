package com.dry_code_snippets.api.Models;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TagTest {

    @Test
    public void testTagCreation() {
        Tag tag = new Tag("Spring Boot");

        assertNotNull(tag);
        assertEquals("Spring Boot", tag.getTagName());
    }

    @Test
    public void testSettersAndGetters() {
        Tag tag = new Tag();
        tag.setTagId(1L);
        tag.setTagName("Java");

        assertEquals(1L, tag.getTagId());
        assertEquals("Java", tag.getTagName());
    }
}
