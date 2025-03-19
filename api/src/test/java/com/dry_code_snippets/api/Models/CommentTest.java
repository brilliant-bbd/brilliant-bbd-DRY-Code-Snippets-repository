package com.dry_code_snippets.api.Models;

import com.dry_code_snippets.api.Models.Comment;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


public class CommentTest {
    @Test
    public void testSnippetRecordCreation() {
        Comment comment = new Comment(1L,2L,"This is a comment");

        assertNotNull(comment);
        assertEquals(1L, comment.getSnippetId());
        assertEquals(2L, comment.getUserId());
        assertEquals("This is a comment", comment.getComment());
    }
}
