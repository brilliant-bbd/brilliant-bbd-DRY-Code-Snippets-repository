package com.dry_code_snippets.api.Services;

import com.dry_code_snippets.api.Models.Version;
import com.dry_code_snippets.api.Repositories.SnippetRepository;
import com.dry_code_snippets.api.Repositories.VersionRepository;
import com.dry_code_snippets.api.Services.VersionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class VersionServiceTest {

    @Mock
    private VersionRepository versionRepository;

    @Mock
    private SnippetRepository snippetRepository;

    @InjectMocks
    private VersionService versionService;

    private Version version1;
    private Version version2;

    @BeforeEach
    void setUp() {
        version1 = new Version(1L, 1L, "Code snippet v1");
        version1.setVersionId(1L);
        version2 = new Version(1L, 2L, "Code snippet v2");
        version2.setVersionId(2L);
    }

    @Test
    void testGetVersionsBySnippetId_ShouldReturnVersions() {
        when(versionRepository.findBySnippetId(1L)).thenReturn(Optional.of(List.of(version1, version2)));

        List<Version> versions = versionService.getVersionsBySnippetId(1L);

        assertNotNull(versions);
        assertEquals(2, versions.size());
        assertEquals(1L, versions.get(0).getVersion());
        assertEquals(2L, versions.get(1).getVersion());

        verify(versionRepository, times(1)).findBySnippetId(1L);
    }

    @Test
    void testGetVersionsBySnippetId_WhenNoVersionsExist_ShouldReturnEmptyList() {
        when(versionRepository.findBySnippetId(1L)).thenReturn(Optional.empty());

        List<Version> versions = versionService.getVersionsBySnippetId(1L);

        assertNotNull(versions);
        assertTrue(versions.isEmpty());

        verify(versionRepository, times(1)).findBySnippetId(1L);
    }

    @Test
    void testCreateVersion_WhenSnippetExists_ShouldSaveVersion() {
        when(snippetRepository.existsById(1L)).thenReturn(true);
        when(versionRepository.findBySnippetId(1L)).thenReturn(Optional.of(List.of(version1)));
        when(versionRepository.save(any(Version.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Version newVersion = versionService.createVersion(1L, "New code");

        assertNotNull(newVersion);
        assertEquals(2L, newVersion.getVersion());
        assertEquals("New code", newVersion.getCode());

        verify(versionRepository, times(1)).save(any(Version.class));
    }

    @Test
    void testCreateVersion_WhenSnippetDoesNotExist_ShouldThrowException() {
        when(snippetRepository.existsById(1L)).thenReturn(false);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            versionService.createVersion(1L, "New code");
        });

        assertEquals("404 NOT_FOUND \"Snippet not found\"", exception.getMessage());

        verify(versionRepository, never()).save(any(Version.class));
    }

    @Test
    void testGetVersionBySnippetIdAndVersionId_WhenVersionExists_ShouldReturnVersion() {
        when(versionRepository.findBySnippetId(1L)).thenReturn(Optional.of(List.of(version1, version2)));

        Version result = versionService.getVersionBySnippetIdAndVersionId(1L, 2L);

        assertNotNull(result);
        assertEquals(2L, result.getVersion());
        assertEquals("Code snippet v2", result.getCode());

        verify(versionRepository, times(1)).findBySnippetId(1L);
    }

    @Test
    void testGetVersionBySnippetIdAndVersionId_WhenVersionDoesNotExist_ShouldThrowException() {
        when(versionRepository.findBySnippetId(1L)).thenReturn(Optional.of(List.of(version1)));

        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> {
            versionService.getVersionBySnippetIdAndVersionId(1L, 3L);
        });

        assertEquals("No version found with ID: 3 for snippet ID: 1", exception.getMessage());
    }

    @Test
    void testGetVersionBySnippetIdAndVersionId_WhenNoVersionsExist_ShouldThrowException() {
        when(versionRepository.findBySnippetId(1L)).thenReturn(Optional.empty());

        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> {
            versionService.getVersionBySnippetIdAndVersionId(1L, 1L);
        });

        assertEquals("No versions found for snippet ID: 1", exception.getMessage());
    }
}
