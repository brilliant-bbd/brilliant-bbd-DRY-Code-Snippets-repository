package com.dry_code_snippets.api.Versions;

import com.dry_code_snippets.api.Models.Version;
import com.dry_code_snippets.api.Repositories.VersionRepository;
import com.dry_code_snippets.api.Services.VersionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class VersionServiceTest {

    @Mock
    private VersionRepository versionRepository;

    @InjectMocks
    private VersionService versionService;

    private Version version1;
    private Version version2;

    @BeforeEach
    void setUp() {
        version1 = new Version(1L, 1L, "Code snippet v1");
        version2 = new Version(1L, 2L, "Code snippet v2");
    }

    @Test
    public void testGetVersionsBySnippetId() {
        when(versionRepository.findBySnippetId(1L)).thenReturn(java.util.Optional.of(List.of(version1, version2)));

        List<Version> versions = versionService.getVersionsBySnippetId(1L);

        assertEquals(2, versions.size());
        verify(versionRepository, times(1)).findBySnippetId(1L);
    }

    /*@Test
    public void testCreateVersion() {
        // Arrange
        when(versionRepository.findBySnippetId(1L)).thenReturn(List.of(version1, version2));
       // when(versionRepository.save(any(Version.class))).thenReturn(version2);

        // Act
        Version createdVersion = versionService.createVersion(1L, "Code snippet v3");
        System.out.println("--------------------------------------------------"+createdVersion.getVersionNum());

        // Assert
        assertEquals(3, createdVersion.getVersionNum()); // New version should be 3
        verify(versionRepository, times(1)).save(any(Version.class));
    }
*/
   /* @Test
    public void testGetVersionBySnippetIdAndVersionId() {
        // Arrange
        when(versionRepository.findBySnippetId(1L)).thenReturn(List.of(version1, version2));

        // Act
        Version retrievedVersion = versionService.getVersionBySnippetIdAndVersionId(1L, 2L);

        // Assert
        assertEquals(2, retrievedVersion.getVersionNum());
    }*/
}
