package com.dry_code_snippets.api.Controllers;

import com.dry_code_snippets.DTO.SnippetDTO;
import com.dry_code_snippets.api.Models.Snippet;
import com.dry_code_snippets.api.Services.AIService;
import com.dry_code_snippets.api.Services.SharedService;
import com.dry_code_snippets.api.Services.SnippetService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class SnippetControllerTest {

    private MockMvc mockMvc;

    @Mock
    private SnippetService snippetService;

    @Mock
    private SharedService sharedService;

    @Mock
    private AIService aiService;

    @InjectMocks
    private SnippetController snippetController;

    private Snippet mockSnippet;
    private SnippetDTO mockSnippetDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(snippetController).build();

        mockSnippet = new Snippet(1L, "Test Snippet", "Test Description", 101L);
        mockSnippetDTO = new SnippetDTO(1L, 1L, "Test Snippet", "Test Description", "Java", "2024-03-20T12:00:00", "System.out.println('Hello');", null, new String[]{"Java"});
    }


    @Test
    void testGetSnippetById_ShouldReturnOk() throws Exception {
        when(snippetService.getSnippetById(1L)).thenReturn(Optional.of(mockSnippetDTO));

        mockMvc.perform(get("/api/snippets/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Test Snippet"));

        verify(snippetService, times(1)).getSnippetById(1L);
    }

    @Test
    void testGetSnippetById_WhenNotFound_ShouldReturnNotFound() throws Exception {
        when(snippetService.getSnippetById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/snippets/1"))
                .andExpect(status().isNotFound());

        verify(snippetService, times(1)).getSnippetById(1L);
    }

    @Test
    void testCreateSnippet_ShouldReturnCreated() throws Exception {
        when(snippetService.createSnippet(any(SnippetDTO.class))).thenReturn(mockSnippet);

        mockMvc.perform(post("/api/snippets")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\":\"Test Snippet\",\"description\":\"Test Description\",\"language\":\"Java\"}"))
                .andExpect(status().isCreated());

        verify(snippetService, times(1)).createSnippet(any(SnippetDTO.class));
    }

    @Test
    void testUpdateSnippet_ShouldReturnOk() throws Exception {
        when(sharedService.resourceExists(1L)).thenReturn(true);
        when(sharedService.ownsResource(1L)).thenReturn(true);
        when(snippetService.updateSnippet(1L, "Updated code")).thenReturn(mockSnippet);

        mockMvc.perform(put("/api/snippets/1")
                .contentType(MediaType.TEXT_PLAIN)
                .content("Updated code"))
                .andExpect(status().isOk());

        verify(snippetService, times(1)).updateSnippet(1L, "Updated code");
    }

    @Test
    void testUpdateSnippet_WhenNotFound_ShouldReturnNotFound() throws Exception {
        when(sharedService.resourceExists(1L)).thenReturn(false);

        mockMvc.perform(put("/api/snippets/1")
                .contentType(MediaType.TEXT_PLAIN)
                .content("Updated code"))
                .andExpect(status().isNotFound());

        verify(snippetService, never()).updateSnippet(anyLong(), anyString());
    }

    @Test
    void testUpdateSnippet_WhenForbidden_ShouldReturnForbidden() throws Exception {
        when(sharedService.resourceExists(1L)).thenReturn(true);
        when(sharedService.ownsResource(1L)).thenReturn(false);

        mockMvc.perform(put("/api/snippets/1")
                .contentType(MediaType.TEXT_PLAIN)
                .content("Updated code"))
                .andExpect(status().isForbidden());

        verify(snippetService, never()).updateSnippet(anyLong(), anyString());
    }

    @Test
    void testDeleteSnippet_ShouldReturnNoContent() throws Exception {
        when(sharedService.resourceExists(1L)).thenReturn(true);
        when(sharedService.ownsResource(1L)).thenReturn(true);
        when(snippetService.deleteSnippet(1L)).thenReturn(1L);

        mockMvc.perform(delete("/api/snippets/1"))
                .andExpect(status().isNoContent());

        verify(snippetService, times(1)).deleteSnippet(1L);
    }

    @Test
    void testDeleteSnippet_WhenNotFound_ShouldReturnNotFound() throws Exception {
        when(sharedService.resourceExists(1L)).thenReturn(false);

        mockMvc.perform(delete("/api/snippets/1"))
                .andExpect(status().isNotFound());

        verify(snippetService, never()).deleteSnippet(anyLong());
    }

}
