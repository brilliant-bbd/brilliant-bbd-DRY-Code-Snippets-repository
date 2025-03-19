package com.dry_code_snippets.api.Controllers;

import com.dry_code_snippets.api.Models.Comment;
import com.dry_code_snippets.api.Services.CommentService;
import com.dry_code_snippets.api.Services.SharedService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/snippets/comments")
public class CommentController {
    private final CommentService commentService;
    private final SharedService sharedService;
    
    public CommentController(CommentService commentService, SharedService sharedService) {
        this.commentService = commentService;
        this.sharedService = sharedService;
    }

    @GetMapping
    public ResponseEntity<List<Comment>> getComments(@RequestParam("snippetId") Long snippetId) {
        return sharedService.resourceExists(snippetId)?
        ResponseEntity.ok(commentService.getCommentsBySnippetId(snippetId)):
        ResponseEntity.notFound().build();
    }

   @PostMapping
    public ResponseEntity<Comment> addComment(@RequestParam("snippetId") Long snippetId, @RequestBody String commentText) {
        Comment createdComment = commentService.addComment(snippetId, commentText);
        return sharedService.resourceExists(snippetId)?
        ResponseEntity.ok(createdComment):
        ResponseEntity.notFound().build();
    }

    @GetMapping("/version")
    public ResponseEntity<List<Comment>> getCommentsByVersion( @RequestParam("snippetId") Long snippetId, @RequestParam("version") Long version) {
        List<Comment> comments = commentService.getCommentsBySnippetIdAndVersion(snippetId, version);

        return sharedService.resourceExists(snippetId) ?
                ResponseEntity.ok(comments) :
                ResponseEntity.notFound().build();
    }
}
