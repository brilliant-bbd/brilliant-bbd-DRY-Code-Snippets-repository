package com.dry_code_snippets.api.Repositories;

import com.dry_code_snippets.api.Models.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query(value = """
    SELECT 
        comment_id, 
        snippet_id, 
        user_id, 
        comment, 
        TO_CHAR((c.created_at AT TIME ZONE 'UTC' AT TIME ZONE 'SAST'), 'YYYY-MM-DD HH24:MI:SS') AS "created_at"
    FROM Comments c
    WHERE snippet_id = :snippetId;
""", nativeQuery = true)
    List<Comment> findCommentsBySnippetId(@Param("snippetId") Long snippetId);
@Query(value = """
    SELECT 
        c.comment_id, 
        c.snippet_id, 
        c.user_id, 
        c.comment, 
        TO_CHAR((v.created_at AT TIME ZONE 'UTC' AT TIME ZONE 'SAST'), 'YYYY-MM-DD HH24:MI:SS') AS "created_at"
    FROM Comments c
    INNER JOIN Versions v ON c.snippet_id = v.snippet_id
    WHERE c.created_at BETWEEN v.created_at
    AND COALESCE(
        (SELECT v1.created_at
         FROM Versions v1
         WHERE v1.version_id = v.version_id + 1
           AND v1.snippet_id = v.snippet_id
         ORDER BY v1.created_at ASC
         LIMIT 1),
        NOW()
    )
    AND v.snippet_id = :snippetId
    AND v.version = :versionNumber;
""", nativeQuery = true)
List<Comment> findCommentsBySnippetIdAndVersion(@Param("snippetId") Long snippetId, @Param("versionNumber") Long versionNumber);
}
