package com.dry_code_snippets.api.Controllers;

import com.dry_code_snippets.api.Models.Comment;
import com.dry_code_snippets.api.Services.CommentService;
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
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class CommentControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CommentService commentService;

    @Mock
    private SharedService sharedService;

    @InjectMocks
    private CommentController commentController;

    private Comment mockComment;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(commentController).build();

        mockComment = new Comment(1L, 2L, "Test comment");
    }

    @Test
    void testGetComments_ShouldReturnOk() throws Exception {
        Comment comment = new Comment(1L, 2L, "Test comment");

        when(sharedService.resourceExists(1L)).thenReturn(true);
        when(commentService.getCommentsBySnippetId(1L)).thenReturn(List.of(comment));

        mockMvc.perform(get("/api/snippets/comments")
                .param("snippetId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].comment").value("Test comment")); 

        verify(commentService, times(1)).getCommentsBySnippetId(1L);
    }

    @Test
    void testGetComments_WhenSnippetDoesNotExist_ShouldReturnNotFound() throws Exception {
        when(sharedService.resourceExists(1L)).thenReturn(false);

        mockMvc.perform(get("/api/snippets/comments")
                .param("snippetId", "1"))
                .andExpect(status().isNotFound());

        verify(commentService, never()).getCommentsBySnippetId(anyLong());
    }

    @Test
    void testAddComment_ShouldReturnOk() throws Exception {
        when(sharedService.resourceExists(1L)).thenReturn(true);
        when(commentService.addComment(1L, "New comment")).thenReturn(mockComment);

        mockMvc.perform(post("/api/snippets/comments")
                .param("snippetId", "1")
                .content("New comment")
                .contentType(MediaType.TEXT_PLAIN))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.comment").value("Test comment"));

        verify(commentService, times(1)).addComment(1L, "New comment");
    }

    @Test
    void testGetCommentsByVersion_ShouldReturnOk() throws Exception {
        when(sharedService.resourceExists(1L)).thenReturn(true);
        when(commentService.getCommentsBySnippetIdAndVersion(1L, 2L)).thenReturn(List.of(mockComment));

        mockMvc.perform(get("/api/snippets/comments/version")
                .param("snippetId", "1")
                .param("version", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].comment").value("Test comment"));

        verify(commentService, times(1)).getCommentsBySnippetIdAndVersion(1L, 2L);
    }
}
