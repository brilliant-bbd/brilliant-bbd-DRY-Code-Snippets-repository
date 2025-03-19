package com.dry_code_snippets.api.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.dry_code_snippets.api.Models.Rating;
import java.util.List;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {
    
    @Query(value = """
    SELECT 
        r.rating_id, 
        r.snippet_id, 
        r.user_id, 
        r.rating, 
        TO_CHAR((r.created_at AT TIME ZONE 'UTC' AT TIME ZONE 'SAST'), 'YYYY-MM-DD HH24:MI:SS')
    FROM Rating r
    WHERE r.snippet_id = :snippetId;
""", nativeQuery = true)
    List<Rating> findRatingsBySnippetId(@Param("snippetId") Long snippetId);
}
