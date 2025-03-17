package com.dry_code_snippets.api.Repositories;

import com.dry_code_snippets.api.Models.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findBySnippetId(Long snippetId);
}