package com.dry_code_snippets.api.Repositories;

import com.dry_code_snippets.api.Models.Version;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface VersionRepository extends JpaRepository<Version, Long> {
    Optional<List<Version>> findBySnippetId(Long snippetId);

    @Query(value = "SELECT v.* FROM versions WHERE snippetId = :snippetId ORDER BY createdAt DESC LIMIT 1", nativeQuery = true)
    Optional<Version> findLatestVersionBySnippetId(@Param("snippetId") Long snippetId);

}
