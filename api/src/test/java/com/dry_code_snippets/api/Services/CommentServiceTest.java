package com.dry_code_snippets.api.Services;

import com.dry_code_snippets.api.Models.Comment;
import com.dry_code_snippets.api.Models.User;
import com.dry_code_snippets.api.Repositories.CommentRepository;
import com.dry_code_snippets.api.Repositories.UserRepository;
import com.dry_code_snippets.api.Services.CommentService;
import com.dry_code_snippets.api.Shared.Shared;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CommentService commentService;

    private UUID mockUserGuid = UUID.randomUUID();

    private Comment mockComment;
    private User mockUser;

    @BeforeEach
    void setUp() {
        mockUser = new User(mockUserGuid);
        mockUser.setUserId(2L);

        mockComment = new Comment(1L, mockUser.getUserId(), "Test comment");
    }

    @Test
    void testGetCommentsBySnippetId_ShouldReturnComments() {
        when(commentRepository.findCommentsBySnippetId(1L)).thenReturn(List.of(mockComment));

        List<Comment> comments = commentService.getCommentsBySnippetId(1L);

        assertNotNull(comments);
        assertEquals(1, comments.size());
        assertEquals("Test comment", comments.get(0).getComment());

        verify(commentRepository, times(1)).findCommentsBySnippetId(1L);
    }

    @Test
    void testGetCommentsBySnippetIdAndVersion_ShouldReturnComments() {
        when(commentRepository.findCommentsBySnippetIdAndVersion(1L, 2L)).thenReturn(List.of(mockComment));

        List<Comment> comments = commentService.getCommentsBySnippetIdAndVersion(1L, 2L);

        assertNotNull(comments);
        assertEquals(1, comments.size());
        assertEquals("Test comment", comments.get(0).getComment());

        verify(commentRepository, times(1)).findCommentsBySnippetIdAndVersion(1L, 2L);
    }

    @Test
    void testAddComment_ShouldSaveComment() {
        when(userRepository.findByUserGuid(Shared.getClaim())).thenReturn(Optional.of(mockUser));
        when(commentRepository.save(any(Comment.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Comment savedComment = commentService.addComment(1L, "New comment");

        assertNotNull(savedComment);
        assertEquals("New comment", savedComment.getComment());
        assertEquals(mockUser.getUserId(), savedComment.getUserId());

        verify(userRepository, times(1)).findByUserGuid(Shared.getClaim());
        verify(commentRepository, times(1)).save(any(Comment.class));
    }

    @Test
    void testAddComment_WhenUserNotFound_ShouldThrowException() {
        when(userRepository.findByUserGuid(Shared.getClaim())).thenReturn(Optional.empty());

        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> {
            commentService.addComment(1L, "New comment");
        });

        verify(commentRepository, never()).save(any(Comment.class));
    }
}
