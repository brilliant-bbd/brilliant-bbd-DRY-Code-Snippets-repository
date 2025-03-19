package com.dry_code_snippets.api.Repositories;

import com.dry_code_snippets.api.Models.Version;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface VersionRepository extends JpaRepository<Version, Long> {
    @Query(value = """
        SELECT
         v.version_id,
         v.snippet_id,
         v.version,
         v.code,
         TO_CHAR((v.created_at AT TIME ZONE 'UTC' AT TIME ZONE 'SAST'), 'YYYY-MM-DD HH24:MI:SS') AS "created_at"
         FROM versions v WHERE v.snippet_id = :snippetId;
        """, nativeQuery = true)
    Optional<List<Version>> findVersionsBySnippetId(Long snippetId);

    @Query(value = """
    SELECT
     v.version_id,
     v.snippet_id,
     v.version,
     v.code,
     TO_CHAR((v.created_at AT TIME ZONE 'UTC' AT TIME ZONE 'SAST'), 'YYYY-MM-DD HH24:MI:SS') AS "created_at"
     FROM versions v WHERE v.snippet_id = :snippetId ORDER BY v.created_at DESC LIMIT 1;
    """, nativeQuery = true)
    Optional<Version> findLatestVersionBySnippetId(@Param("snippetId") Long snippetId);

}
