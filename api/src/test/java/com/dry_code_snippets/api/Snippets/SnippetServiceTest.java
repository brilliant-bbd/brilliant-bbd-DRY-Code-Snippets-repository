package com.dry_code_snippets.api.Snippets;

import com.dry_code_snippets.DTO.SnippetDTO;
import com.dry_code_snippets.api.Models.Language;
import com.dry_code_snippets.api.Models.Snippet;
import com.dry_code_snippets.api.Models.User;
import com.dry_code_snippets.api.Models.Version;
import com.dry_code_snippets.api.Repositories.LanguageRepository;
import com.dry_code_snippets.api.Repositories.SnippetRepository;
import com.dry_code_snippets.api.Repositories.UserRepository;
import com.dry_code_snippets.api.Repositories.VersionRepository;
import com.dry_code_snippets.api.Services.SnippetService;
import com.dry_code_snippets.api.Services.UserService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

class SnippetServiceTest {
 @Mock
    private SnippetRepository snippetRepository;

    @Mock
    private LanguageRepository languageRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private VersionRepository versionRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private SnippetService snippetService;

    private SnippetDTO snippetDTO;
    private Snippet snippet;
    private Language language;
    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        snippet = new Snippet(
                1L,
                "Test Title",
                "Test Description",
                1);
        String[] myArray1 = {"apple", "test", "cherry"};
        snippetDTO = new SnippetDTO(2, 1, "some title", "some description", "java", Timestamp.valueOf(LocalDateTime.now()).toString(),
                "this is the code",BigDecimal.ONE,myArray1);

                  Optional<Language> language = Optional.of(new Language("java"));
        language.get().setLanguageId(1);
        language.get().setLanguageName("java");
        when(languageRepository.findByLanguageName("java")).thenReturn(java.util.Optional.of(new Language("java")));
        when(userService.getClaim()).thenReturn(UUID.fromString("123e4567-e89b-12d3-a456-426614174000")); 

        user = new User(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"));
        user.setUserId(1L);
    }

    @Test
    void testGetAllSnippets() {
        when(snippetRepository.findAll()).thenReturn(List.of(snippet));

        List<SnippetDTO> snippets = snippetService.getAllSnippets(null, null);

        assertNotNull(snippets);
        assertEquals(1, snippets.size());
        assertEquals("Test Title", snippets.get(0).getTitle());
        verify(snippetRepository, times(1)).findAll();
    }

    @Test
    void testGetSnippetById() {
        when(snippetRepository.findById(1L)).thenReturn(Optional.of(snippet));

        Optional<SnippetDTO> foundSnippet = Optional.of(snippetService.getSnippetById(1L));

        assertTrue(foundSnippet.isPresent());
        assertEquals("Test Title", foundSnippet.get().getTitle());
        verify(snippetRepository, times(1)).findById(1L);
    }

    @Test
    void testCreateSnippet() {
        
        when(languageRepository.findByLanguageName("java")).thenReturn(Optional.of(language));

        when(userService.getClaim()).thenReturn(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"));

        when(userRepository.findByUserGuid(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"))).thenReturn(Optional.of(user));

        when(snippetRepository.save(any(Snippet.class))).thenReturn(snippet);

        Snippet createdSnippet = snippetService.createSnippet(snippetDTO);

        assertNotNull(createdSnippet);
        assertEquals("Test Title", createdSnippet.getTitle()); 

        verify(languageRepository, times(1)).findByLanguageName("java");
        verify(userService, times(1)).getClaim();
        verify(userRepository, times(1)).findByUserGuid(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"));
        verify(snippetRepository, times(1)).save(any(Snippet.class));
        verify(versionRepository, times(1)).save(any(Version.class));
    }

    @Test
    void testUpdateSnippet() {
        when(snippetRepository.existsById(1L)).thenReturn(true);
        when(snippetRepository.save(any(Snippet.class))).thenReturn(snippet);
        Snippet updatedSnippet = snippetService.updateSnippet(1L, "code");

        assertNotNull(updatedSnippet);
        assertEquals("Test Title", updatedSnippet.getTitle());
        verify(snippetRepository, times(1)).existsById(1L);
        verify(snippetRepository, times(1)).save(any(Snippet.class));
    }

    @Test
    void testUpdateSnippetNotFound() {
        when(snippetRepository.existsById(1L)).thenReturn(false);

        Snippet updatedSnippet = snippetService.updateSnippet(1L, "code");

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
