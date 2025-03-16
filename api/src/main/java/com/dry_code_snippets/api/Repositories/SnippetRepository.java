package com.dry_code_snippets.api.Repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.dry_code_snippets.api.Models.Snippet;

@Repository
public interface SnippetRepository extends JpaRepository<Snippet, Long> {
    Snippet findSnippetByDescription(String description);
     // Custom query to filter snippets by language and deleted status
    @Query("SELECT s FROM Snippet s WHERE s.language = :language AND s.deleted = false")
    List<Snippet> findByLanguageAndNotDeleted(@Param("language") String language);

    // Custom query to filter snippets by tags and deleted status
    @Query("SELECT s FROM Snippet s JOIN s.tags t WHERE t.name IN :tags AND s.deleted = false")
    List<Snippet> findByTagsAndNotDeleted(@Param("tags") List<String> tags);

    // Custom query to get snippets by language, tags, and deleted status
    @Query("SELECT s FROM Snippet s JOIN s.tags t WHERE s.language = :language AND t.name IN :tags AND s.deleted = false")
    List<Snippet> findByLanguageAndTagsAndNotDeleted(@Param("language") String language, @Param("tags") List<String> tags);

    // Fallback method to get all non-deleted snippets (no filters)
    List<Snippet> findAllByDeletedFalse();
}

