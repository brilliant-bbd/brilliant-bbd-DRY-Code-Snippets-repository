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
        Snippet snippet = new Snippet("userId", "Test Snippet", "This is a test description", "System.out.println(\"Hello World\");", 1);
        
        Snippet savedSnippet = snippetRepository.save(snippet);

        assertNotNull(savedSnippet);
        assertNotNull(savedSnippet.getSnippetId());
    }

    @Test
    public void testCreateSnippetWithNullTitle() {
        Snippet snippet = new Snippet(
            "userid", 
            null, // Null title, should trigger constraint violation
            "This is a test description",
            "System.out.println(\"Hello World\");", 
            1
        );

        assertThrows(DataIntegrityViolationException.class, () -> {
            snippetRepository.save(snippet);
        });
    }
    
    @Test
    public void testCreateSnippetWithNullCode() {
        Snippet snippet = new Snippet(
            "userid", 
            "Test Title", 
            "This is a test description", 
            null, // Null code, should trigger constraint violation
            1
        );

        assertThrows(DataIntegrityViolationException.class, () -> {
            snippetRepository.save(snippet);
        });
    }

    @Test
    public void testCreateSnippetWithNullUpdatedAt() {
        Snippet snippet = new Snippet(
            "userid", 
            "Test Title", 
            "This is a test description",
            "System.out.println(\"Hello World\");", 
            1
        );

        assertThrows(DataIntegrityViolationException.class, () -> {
            snippetRepository.save(snippet);
        });
    }

    @Test
    public void testCreateSnippetWithNullIsDeleted() {
        Snippet snippet = new Snippet(
            "userid", 
            "Test Title", 
            "This is a test description",
            "System.out.println(\"Hello World\");", 
            1
        );

        assertThrows(DataIntegrityViolationException.class, () -> {
            snippetRepository.save(snippet);
        });
    }
}