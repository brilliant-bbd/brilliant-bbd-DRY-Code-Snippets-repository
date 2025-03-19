package com.dry_code_snippets.api.Controllers;

import com.dry_code_snippets.api.Models.Version;
import com.dry_code_snippets.api.Services.SharedService;
import com.dry_code_snippets.api.Services.VersionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class VersionControllerTest {

    private MockMvc mockMvc;

    @Mock
    private VersionService versionService;

    @Mock
    private SharedService sharedService;

    @InjectMocks
    private VersionController versionController;

    private Version mockVersion;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(versionController).build();

        mockVersion = new Version(1L, 2L, "System.out.println(\"Hello World\");");
    }

    @Test
    void testGetVersionsBySnippetId_ShouldReturnOk() throws Exception {
        when(sharedService.resourceExists(1L)).thenReturn(true);
        when(versionService.getVersionsBySnippetId(1L)).thenReturn(List.of(mockVersion));

        mockMvc.perform(get("/api/snippets/versions")
                .param("snippetId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].version").value(2));

        verify(versionService, times(1)).getVersionsBySnippetId(1L);
    }

    @Test
    void testGetVersionsBySnippetId_WhenSnippetDoesNotExist_ShouldReturnNotFound() throws Exception {
        when(sharedService.resourceExists(1L)).thenReturn(false);

        mockMvc.perform(get("/api/snippets/versions")
                .param("snippetId", "1"))
                .andExpect(status().isNotFound());

        verify(versionService, never()).getVersionsBySnippetId(anyLong());
    }

    @Test
    void testGetVersionBySnippetIdAndVersionId_ShouldReturnOk() throws Exception {
        when(sharedService.resourceExists(1L)).thenReturn(true);
        when(versionService.getVersionBySnippetIdAndVersionId(1L, 2L)).thenReturn(mockVersion);

        mockMvc.perform(get("/api/snippets/versions/byVersion")
                .param("snippetId", "1")
                .param("version", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.version").value(2));

        verify(versionService, times(1)).getVersionBySnippetIdAndVersionId(1L, 2L);
    }

    @Test
    void testGetVersionBySnippetIdAndVersionId_WhenSnippetDoesNotExist_ShouldReturnNotFound() throws Exception {
        when(sharedService.resourceExists(1L)).thenReturn(false);

        mockMvc.perform(get("/api/snippets/versions/byVersion")
                .param("snippetId", "1")
                .param("version", "2"))
                .andExpect(status().isNotFound());

        verify(versionService, never()).getVersionBySnippetIdAndVersionId(anyLong(), anyLong());
    }

    @Test
    void testGetVersionBySnippetIdAndVersionId_WhenVersionNotFound_ShouldReturnNotFound() throws Exception {
        when(sharedService.resourceExists(1L)).thenReturn(true);
        when(versionService.getVersionBySnippetIdAndVersionId(1L, 2L)).thenThrow(new RuntimeException("Version not found"));

        mockMvc.perform(get("/api/snippets/versions/byVersion")
                .param("snippetId", "1")
                .param("version", "2"))
                .andExpect(status().isNotFound());

        verify(versionService, times(1)).getVersionBySnippetIdAndVersionId(1L, 2L);
    }
}
