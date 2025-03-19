package com.dry_code_snippets.api.Models;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LanguageTest {

    @Test
    public void testLanguageCreation() {
        Language language = new Language("Java");

        assertNotNull(language);
        assertEquals("Java", language.getLanguageName());
    }

    @Test
    public void testSettersAndGetters() {
        Language language = new Language();
        language.setLanguageId(1L);
        language.setLanguageName("Python");

        assertEquals(1L, language.getLanguageId());
        assertEquals("Python", language.getLanguageName());
    }
}
