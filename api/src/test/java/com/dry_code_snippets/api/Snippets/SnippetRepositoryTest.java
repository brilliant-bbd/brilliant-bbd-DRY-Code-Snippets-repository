package com.dry_code_snippets.api.Snippets;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import com.dry_code_snippets.api.Models.Snippet;
import com.dry_code_snippets.api.Repositories.SnippetRepository;

import static org.junit.jupiter.api.Assertions.*;


@DataJpaTest
public class SnippetRepositoryTest {

    @Autowired
    private SnippetRepository snippetRepository;

    @Test
    public void testCreateSnippetWithRequiredFields() {
        Snippet snippet = new Snippet(1L, "Test Snippet", "This is a test description",  1);
        
        Snippet savedSnippet = snippetRepository.save(snippet);

        assertNotNull(savedSnippet);
        assertNotNull(savedSnippet.getSnippetId());
    }

    @Test
    public void testCreateSnippetWithNullTitle() {
        Snippet snippet = new Snippet(
            1L, 
            null,
            "This is a test description",
            1
        );

        assertThrows(DataIntegrityViolationException.class, () -> {
            snippetRepository.save(snippet);
        });
    }
}