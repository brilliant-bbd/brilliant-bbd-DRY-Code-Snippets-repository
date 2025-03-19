package com.dry_code_snippets.api.Versions;

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
import java.util.List;
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
    public void testGetVersionsBySnippetId() {
        when(versionRepository.findVersionsBySnippetId(1L)).thenReturn(Optional.of(List.of(version1, version2)));

        List<Version> versions = versionService.getVersionsBySnippetId(1L);

        assertEquals(2, versions.size());
        verify(versionRepository, times(1)).findVersionsBySnippetId(1L);
    }

    @Test
    public void testCreateVersion() {
        Long snippetId = 1L;
        String code = "Code snippet v3";

        Version mockVersion = new Version();
        mockVersion.setSnippetId(snippetId);
        mockVersion.setCode(code);

        when(versionRepository.findVersionsBySnippetId(1L)).thenReturn(Optional.of(List.of(version1, version2)));
        when(versionRepository.save(any(Version.class))).thenReturn(mockVersion);
        when(snippetRepository.existsById(snippetId)).thenReturn(true);


        Version savedVersion = versionService.createVersion(snippetId, code);

        assertNotNull(savedVersion);
        assertEquals(snippetId, savedVersion.getSnippetId());
        assertEquals(code, savedVersion.getCode());

        verify(versionRepository, times(1)).save(any(Version.class));
    }


    @Test
    public void testGetVersionBySnippetIdAndVersionId() {
        when(versionRepository.findVersionsBySnippetId(1L)).thenReturn(Optional.of(List.of(version1, version2)));

        Version retrievedVersion = versionService.getVersionBySnippetIdAndVersionId(1L, 2L);

        assertNotNull(retrievedVersion);
        assertEquals(2, retrievedVersion.getVersion());
    }

}
