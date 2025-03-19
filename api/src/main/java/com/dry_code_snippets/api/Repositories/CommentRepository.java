package com.dry_code_snippets.api.Repositories;

import com.dry_code_snippets.api.Models.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findBySnippetId(Long snippetId);
@Query(value = """
    SELECT c.*
    FROM Comments c
    INNER JOIN Versions v ON c.snippetID = v.snippetID
    WHERE c.createdAt BETWEEN v.createdAt
    AND COALESCE(
        (SELECT v1.createdAt
         FROM Versions v1
         WHERE v1.versionID = v.versionID + 1
           AND v1.snippetID = v.snippetID
         ORDER BY v1.createdAt ASC
         LIMIT 1),
        NOW()
    )
    AND v.snippetID = :snippetId
    AND v.version = :versionNumber
""", nativeQuery = true)
List<Comment> findCommentsBySnippetIdAndVersion(@Param("snippetId") Long snippetId, @Param("versionNumber") Long versionNumber);
}
