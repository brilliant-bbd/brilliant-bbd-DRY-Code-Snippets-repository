package com.dry_code_snippets.api.Repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.dry_code_snippets.api.Models.Snippet;

@Repository
public interface SnippetRepository extends JpaRepository<Snippet, Long> {
    // Custom queries can be added here if needed
    // @Query("SELECT s FROM Snippet s WHERE s.userId = :userId")
    // List<Snippet> findByUserId(Long userId);
    Snippet findSnippetByDescription(String description);
}