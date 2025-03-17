package com.dry_code_snippets.api.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.dry_code_snippets.api.Models.Rating;
import java.util.List;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {
    List<Rating> findBySnippetId(Long snippetId);
}
