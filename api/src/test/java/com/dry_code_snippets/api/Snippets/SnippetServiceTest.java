package com.dry_code_snippets.api.Snippets;

import com.dry_code_snippets.DTO.SnippetDTO;
import com.dry_code_snippets.api.Models.Language;
import com.dry_code_snippets.api.Models.Snippet;
import com.dry_code_snippets.api.Models.SnippetTag;
import com.dry_code_snippets.api.Models.User;
import com.dry_code_snippets.api.Models.Tag;
import com.dry_code_snippets.api.Models.Version;
import com.dry_code_snippets.api.Repositories.LanguageRepository;
import com.dry_code_snippets.api.Repositories.SnippetRepository;
import com.dry_code_snippets.api.Repositories.TagRepository;
import com.dry_code_snippets.api.Repositories.UserRepository;
import com.dry_code_snippets.api.Repositories.VersionRepository;
import com.dry_code_snippets.api.Repositories.SnippetTagRepository;
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
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
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

    @Mock
    private TagRepository tagRepository;

    @Mock
    private SnippetTagRepository snippetTagRepository;

    @InjectMocks
    private SnippetService snippetService;

    private SnippetDTO snippetDTO;
    private Snippet snippet;
    private Language language;
    private User user;
    private Tag tag;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        snippet = new Snippet(
                1L,
                "Test Title",
                "Test Description",
                1L);
        snippet.setUserId(1l);

        String[] myArray1 = {"Java"};

        snippetDTO = new SnippetDTO(2L, 1L, "some title", "some description", "java", LocalDateTime.now().toString(),
                "this is the code",BigDecimal.ONE,myArray1);

        language = new Language("java");
        language.setLanguageId(1L);
        language.setLanguageName("java");

        when(languageRepository.findByLanguageName("java")).thenReturn(java.util.Optional.of(new Language("java")));

        when(userService.getClaim()).thenReturn(UUID.fromString("123e4567-e89b-12d3-a456-426614174000")); 

        user = new User(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"));
        user.setUserId(1L);

        tag = new Tag();
        tag.setTagId(1L);
        tag.setTagName("Java");

        when(tagRepository.findById(1l)).thenReturn(Optional.of(tag));
    }

    @Test
    void testGetAllSnippets() {
        when(snippetRepository.findSnippetsWithNullTagsAndNullLanguage()).thenReturn(List.of(snippetDTO));
        List<SnippetDTO> snippets = snippetService.getAllSnippets(Optional.empty(), Optional.empty());

        assertNotNull(snippets);
        assertEquals(1, snippets.size());
        assertEquals("some title", snippets.get(0).getTitle());
        verify(snippetRepository, times(1)).findSnippetsWithNullTagsAndNullLanguage(); 
    }

    @Test
    void testGetAllSnippetsWithTag() {
        when(snippetRepository.findSnippetsWithTagsAndLanguageAndNullLanguage(Arrays.asList(tag.getTagName()))).thenReturn(List.of(snippetDTO));
        List<SnippetDTO> snippets = snippetService.getAllSnippets(Optional.of(tag.getTagName()), Optional.empty());

        assertNotNull(snippets);
        assertEquals(1, snippets.size());
        assertEquals("some title", snippets.get(0).getTitle());
        verify(snippetRepository, times(1)).findSnippetsWithTagsAndLanguageAndNullLanguage(Arrays.asList(tag.getTagName())); 
    }

    @Test
    void testGetAllSnippetsWithLanguage() {
        when(snippetRepository.findSnippetsWithNullTagsAndLanguage(language.getLanguageName())).thenReturn(List.of(snippetDTO));
        List<SnippetDTO> snippets = snippetService.getAllSnippets(Optional.empty(), Optional.of(language.getLanguageName()));

        assertNotNull(snippets);
        assertEquals(1, snippets.size());
        assertEquals("some title", snippets.get(0).getTitle());
        verify(snippetRepository, times(1)).findSnippetsWithNullTagsAndLanguage(language.getLanguageName()); 
    }

    @Test
    void testGetAllSnippetsWithTagAndLanguage() {
        when(snippetRepository.findSnippetsWithTagsAndLanguage(language.getLanguageName(), Arrays.asList(tag.getTagName()))).thenReturn(List.of(snippetDTO));
        List<SnippetDTO> snippets = snippetService.getAllSnippets(Optional.of(tag.getTagName()), Optional.of(language.getLanguageName()));

        assertNotNull(snippets);
        assertEquals(1, snippets.size());
        assertEquals("some title", snippets.get(0).getTitle());
        verify(snippetRepository, times(1)).findSnippetsWithTagsAndLanguage(language.getLanguageName(), Arrays.asList(tag.getTagName())); 
    }

    @Test
    void testGetSnippetById() {
        when(snippetRepository.findSnippetDtoById(1L)).thenReturn(Optional.of(snippetDTO));

        Optional<SnippetDTO> foundSnippet = snippetService.getSnippetById(1L);

        assertTrue(foundSnippet.isPresent());
        assertEquals("some title", foundSnippet.get().getTitle());
        verify(snippetRepository, times(1)).findSnippetDtoById(1L);
    }

    @Test
    void testCreateSnippet() {
        
        when(languageRepository.findByLanguageName("java")).thenReturn(Optional.of(language));

        when(userService.getClaim()).thenReturn(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"));

        when(userRepository.findByUserGuid(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"))).thenReturn(Optional.of(user));

        when(snippetRepository.save(any(Snippet.class))).thenReturn(snippet);

        when(tagRepository.save(any(Tag.class))).thenReturn(tag);

        when(snippetTagRepository.save(any(SnippetTag.class))).thenReturn(new SnippetTag(1L, 1L));

        when(versionRepository.save(any(Version.class))).thenReturn(new Version(1L, 1L, "System.out.println('Hello, world!');"));

        Snippet createdSnippet = snippetService.createSnippet(snippetDTO);

        assertNotNull(createdSnippet);
        assertEquals("Test Title", createdSnippet.getTitle()); 

        verify(languageRepository, times(1)).findByLanguageName("java");
        verify(userService, times(1)).getClaim();
        verify(userRepository, times(1)).findByUserGuid(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"));
        verify(snippetRepository, times(1)).save(any(Snippet.class));
        verify(tagRepository, times(1)).save(any(Tag.class)); 
        verify(snippetTagRepository, times(1)).save(any(SnippetTag.class)); 
        verify(versionRepository, times(1)).save(any(Version.class));

    }

    @Test
    void testUpdateSnippet() {
        Version latestVersion = new Version(1L, 1L, "oldCode");

        when(snippetRepository.findById(1L)).thenReturn(Optional.of(snippet));
        when(userService.getClaim()).thenReturn(UUID.randomUUID());
        when(userRepository.findByUserGuid(any(UUID.class))).thenReturn(Optional.of(user));
        when(versionRepository.findLatestVersionBySnippetId(1L)).thenReturn(Optional.of(latestVersion));
        when(versionRepository.save(any(Version.class))).thenReturn(new Version(1L, 2L, "newCode"));

        Snippet result = snippetService.updateSnippet(1L, "newCode");

        assertNotNull(result);
        assertEquals("Test Title", result.getTitle()); 
        verify(versionRepository, times(1)).save(any(Version.class));
    }

    @Test
    void testUpdateSnippetNotFound() {
        when(snippetRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            snippetService.updateSnippet(1L, "new code for me");
        });

        assertEquals("Snippet not found with ID: 1", exception.getMessage());
    }

    @Test
    void testUpdateSnippetUserNotFound() {
        when(snippetRepository.findById(1L)).thenReturn(Optional.of(snippet));
        when(userService.getClaim()).thenReturn(UUID.randomUUID());
        when(userRepository.findByUserGuid(any(UUID.class))).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalStateException.class, () -> {
            snippetService.updateSnippet(1L, "new code for me");
        });

        assertEquals("User not found", exception.getMessage());
    }

    @Test
    void testUpdateSnippetNotTheCreator() {
        when(snippetRepository.findById(1L)).thenReturn(Optional.of(snippet));
        when(userService.getClaim()).thenReturn(UUID.randomUUID());

        User user1 = new User(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"));
        user1.setUserId(2L);

        when(userRepository.findByUserGuid(any(UUID.class))).thenReturn(Optional.of(user1));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            snippetService.updateSnippet(1L, "new code for me");
        });

        assertEquals("You do not have permission to update this snippet", exception.getMessage());
    }

    @Test
    void testDeleteSnippet() {
        doAnswer(invocation -> {
            return null;
        }).when(snippetRepository).softDeleteById(1L);

        snippetService.deleteSnippet(1L);

        verify(snippetRepository, times(1)).softDeleteById(1L);
    }
}
