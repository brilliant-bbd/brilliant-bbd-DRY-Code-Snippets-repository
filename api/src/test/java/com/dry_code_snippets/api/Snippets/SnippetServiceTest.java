package com.dry_code_snippets.api.Snippets;

import com.dry_code_snippets.DTO.SnippetDTO;
import com.dry_code_snippets.api.Models.Snippet;
import com.dry_code_snippets.api.Repositories.SnippetRepository;
import com.dry_code_snippets.api.Services.SnippetService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

class SnippetServiceTest {

    @Mock
    private SnippetRepository snippetRepository;

    @InjectMocks
    private SnippetService snippetService;

    private Snippet snippet;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        snippet = new Snippet(
            1L,
            "Test Title",
            "Test Description",
            1
        );
    }

    @Test
    void testGetAllSnippets() {
        when(snippetRepository.findAll()).thenReturn(List.of(snippet));

        List<Snippet> snippets = snippetService.getAllSnippets(String tag, String language);

        assertNotNull(snippets);
        assertEquals(1, snippets.size());
        assertEquals("Test Title", snippets.get(0).getTitle());
        verify(snippetRepository, times(1)).findAll();
    }

    @Test
    void testGetSnippetById() {
        when(snippetRepository.findById(1L)).thenReturn(Optional.of(snippet));

        Optional<Snippet> foundSnippet = snippetService.getSnippetById(1L);

        assertTrue(foundSnippet.isPresent());
        assertEquals("Test Title", foundSnippet.get().getTitle());
        verify(snippetRepository, times(1)).findById(1L);
    }

    @Test
    void testCreateSnippet() {
        when(snippetRepository.save(any(Snippet.class))).thenReturn(snippet);
        SnippetDTO snippetDTO = new SnippetDTO(2L, 1L, "some title", "some description", "java", LocalDateTime.now(), "this is the code");
        Snippet createdSnippet = snippetService.createSnippet(snippetDTO);

        assertNotNull(createdSnippet);
        assertEquals("Test Title", createdSnippet.getTitle());
        verify(snippetRepository, times(1)).save(snippet);
    }

    @Test
    void testUpdateSnippet() {
        when(snippetRepository.existsById(1L)).thenReturn(true);
        when(snippetRepository.save(any(Snippet.class))).thenReturn(snippet);

        Snippet updatedSnippet = snippetService.updateSnippet(1L, snippet);

        assertNotNull(updatedSnippet);
        assertEquals("Test Title", updatedSnippet.getTitle());
        verify(snippetRepository, times(1)).existsById(1L);
        verify(snippetRepository, times(1)).save(any(Snippet.class));
    }

    @Test
    void testUpdateSnippetNotFound() {
        when(snippetRepository.existsById(1L)).thenReturn(false);

        Snippet updatedSnippet = snippetService.updateSnippet(1L, snippet);

        assertNull(updatedSnippet);
        verify(snippetRepository, times(1)).existsById(1L);
    }

    @Test
    void testDeleteSnippet() {
        doNothing().when(snippetRepository).deleteById(1L);

        snippetService.deleteSnippet(1L);

        verify(snippetRepository, times(1)).deleteById(1L);
    }
}
