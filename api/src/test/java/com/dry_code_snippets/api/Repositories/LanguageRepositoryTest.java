package com.dry_code_snippets.api.Repositories;

import com.dry_code_snippets.api.Models.Language;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class LanguageRepositoryTest {

    @Autowired
    private LanguageRepository languageRepository;

    private Language language;

    @BeforeEach
    void setUp() {
        // Setup test data before each test
        language = new Language("Java");
        languageRepository.save(language);
    }

    @AfterEach
    void tearDown() {
        // Clean up after each test
        languageRepository.deleteAll();
    }

    @Test
    void testFindByLanguageName_ShouldReturnLanguage() {
        Optional<Language> foundLanguage = languageRepository.findByLanguageName("Java");

        assertTrue(foundLanguage.isPresent());
        assertEquals("Java", foundLanguage.get().getLanguageName());
    }

    @Test
    void testFindByLanguageName_WhenLanguageDoesNotExist_ShouldReturnEmpty() {
        Optional<Language> foundLanguage = languageRepository.findByLanguageName("Python");

        assertFalse(foundLanguage.isPresent());
    }

    @Test
    void testSaveLanguage_ShouldPersistLanguage() {
        Language newLanguage = new Language("Python");
        Language savedLanguage = languageRepository.save(newLanguage);

        assertNotNull(savedLanguage.getLanguageId());
        assertEquals("Python", savedLanguage.getLanguageName());
    }
}
