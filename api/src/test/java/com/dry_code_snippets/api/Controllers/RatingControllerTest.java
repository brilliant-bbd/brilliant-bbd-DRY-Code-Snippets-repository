package com.dry_code_snippets.api.Controllers;

import com.dry_code_snippets.api.Models.Rating;
import com.dry_code_snippets.api.Services.RatingService;
import com.dry_code_snippets.api.Services.SharedService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class RatingControllerTest {

    private MockMvc mockMvc;

    @Mock
    private RatingService ratingService;

    @Mock
    private SharedService sharedService;

    @InjectMocks
    private RatingController ratingController;

    private Rating mockRating;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(ratingController).build();

        mockRating = new Rating(1L, 2L, 5);
    }

    @Test
    void testAddRating_WhenSnippetExists_ShouldReturnCreated() throws Exception {
        when(sharedService.resourceExists(1L)).thenReturn(true);
        when(ratingService.addRating(1L, 5)).thenReturn(mockRating);

        mockMvc.perform(post("/api/snippets/ratings")
                .param("snippetId", "1")
                .content("5")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.rating").value(5));

        verify(ratingService, times(1)).addRating(1L, 5);
    }

    @Test
    void testAddRating_WhenSnippetDoesNotExist_ShouldReturnNotFound() throws Exception {
        when(sharedService.resourceExists(1L)).thenReturn(false);

        mockMvc.perform(post("/api/snippets/ratings")
                .param("snippetId", "1")
                .content("5")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(ratingService, never()).addRating(anyLong(), anyInt());
    }

    @Test
    void testGetRatings_WhenSnippetExists_ShouldReturnOk() throws Exception {
        when(sharedService.resourceExists(1L)).thenReturn(true);
        when(ratingService.getRatingsForSnippet(1L)).thenReturn(List.of(mockRating));

        mockMvc.perform(get("/api/snippets/ratings")
                .param("snippetId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].rating").value(5));

        verify(ratingService, times(1)).getRatingsForSnippet(1L);
    }

    @Test
    void testGetRatings_WhenSnippetDoesNotExist_ShouldReturnNotFound() throws Exception {
        when(sharedService.resourceExists(1L)).thenReturn(false);

        mockMvc.perform(get("/api/snippets/ratings")
                .param("snippetId", "1"))
                .andExpect(status().isNotFound());

        verify(ratingService, never()).getRatingsForSnippet(anyLong());
    }
}
