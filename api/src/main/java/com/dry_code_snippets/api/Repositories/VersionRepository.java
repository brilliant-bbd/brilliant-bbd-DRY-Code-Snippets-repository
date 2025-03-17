package com.dry_code_snippets.api.Repositories;

import com.dry_code_snippets.api.Models.Version;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface VersionRepository extends JpaRepository<Version, Long> {
    Optional<List<Version>> findBySnippetId(Long snippetId);

    Optional<Version> findMaxVersionByVersionId(Long snippetId);
}
