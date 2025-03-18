package com.dry_code_snippets.api.Controllers;

import com.dry_code_snippets.api.Models.Comment;
import com.dry_code_snippets.api.Services.CommentService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/snippets/comments")
public class CommentController {
    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping
    public ResponseEntity<List<Comment>> getComments(@RequestParam("snippetId") Long snippetId) {
        return ResponseEntity.ok(commentService.getCommentsBySnippetId(snippetId));
    }

   @PostMapping
    public ResponseEntity<Comment> addComment(@RequestParam("snippetId") Long snippetId, @RequestBody String commentText) {
        Comment createdComment = commentService.addComment(snippetId, commentText);
        return new ResponseEntity<>(createdComment, HttpStatus.CREATED);
    }
}
