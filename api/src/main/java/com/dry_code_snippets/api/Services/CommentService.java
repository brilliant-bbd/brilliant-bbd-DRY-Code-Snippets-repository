package com.dry_code_snippets.api.Services;

import com.dry_code_snippets.api.Models.Comment;
import com.dry_code_snippets.api.Models.User;
import com.dry_code_snippets.api.Repositories.CommentRepository;
import com.dry_code_snippets.api.Repositories.UserRepository;
import com.dry_code_snippets.api.Shared.Shared;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;


@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;


    @Autowired
    public CommentService(CommentRepository commentRepository, UserRepository userRepository) {
        this.commentRepository = commentRepository;

        this.userRepository = userRepository;
    }

    public List<Comment> getCommentsBySnippetId(Long snippetId) {
        return commentRepository.findBySnippetId(snippetId);
    }

    public Comment addComment(Long snippetId, String commentText) {
        User user = userRepository.findByUserGuid(Shared.getClaim());
        Comment comment = new Comment(snippetId,user.getUserId(),commentText);
        return commentRepository.save(comment);
    }

}