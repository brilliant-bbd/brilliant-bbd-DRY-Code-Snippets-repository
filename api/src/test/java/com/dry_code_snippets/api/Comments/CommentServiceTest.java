package com.dry_code_snippets.api.Comments;

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
import org.mockito.junit.jupiter.MockitoExtension;

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

    private Optional<User> mockUser;
    private UUID mockUserGuid = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        mockUser = Optional.of(new User());
        mockUser.get().setUserId(1L);

        when(userRepository.findByUserGuid(mockUserGuid)).thenReturn(mockUser);
    }

    @Test
    void testAddComment() {
        Long snippetId = 1L;
        String commentText = "This is a test comment.";

        Comment mockComment = new Comment(snippetId, mockUser.get().getUserId(), commentText);
        when(commentRepository.save(any(Comment.class))).thenReturn(mockComment);

        try (MockedStatic<Shared> mockedStatic = mockStatic(Shared.class)) {
            mockedStatic.when(Shared::getClaim).thenReturn(mockUserGuid);

            Comment savedComment = commentService.addComment(snippetId, commentText);

            assertNotNull(savedComment);
            assertEquals(snippetId, savedComment.getSnippetId());
            assertEquals(mockUser.get().getUserId(), savedComment.getUserId());
            assertEquals(commentText, savedComment.getComment());

            verify(userRepository, times(1)).findByUserGuid(mockUserGuid);
            verify(commentRepository, times(1)).save(any(Comment.class));
        }
    }
}
