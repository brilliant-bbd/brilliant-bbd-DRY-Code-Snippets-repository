package com.dry_code_snippets.api.Comments;

import com.dry_code_snippets.api.Models.Comment;
import com.dry_code_snippets.api.Repositories.CommentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Transactional
@Rollback
class CommentRepositoryTest {

    @Autowired
    private CommentRepository commentRepository;

    private Comment comment1, comment2;

    @BeforeEach
    void setUp() {
        comment1 = new Comment(1L, 101L, "This is a test comment.");
        comment2 = new Comment(1L, 102L, "Another test comment.");
        commentRepository.save(comment1);
        commentRepository.save(comment2);
    }

    @Test
    void testFindBySnippetId() {
        List<Comment> comments = commentRepository.findBySnippetId(1L);
        assertEquals(2, comments.size());
        assertTrue(comments.stream().anyMatch(c -> c.getComment().equals("This is a test comment.")));
        assertTrue(comments.stream().anyMatch(c -> c.getComment().equals("Another test comment.")));
    }

    @Test
    void testSaveComment() {
        Comment comment = new Comment(2L, 103L, "New comment for another snippet.");
        Comment savedComment = commentRepository.save(comment);

        assertNotNull(savedComment.getCommentId());
        assertEquals(2L, savedComment.getSnippetId());
        assertEquals("New comment for another snippet.", savedComment.getComment());
    }
}
