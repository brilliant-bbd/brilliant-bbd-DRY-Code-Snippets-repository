package com.dry_code_snippets.api.Repositories;

import com.dry_code_snippets.api.Models.Version;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface VersionRepository extends JpaRepository<Version, Long> {
    List<Version> findBySnippetId(Long snippetId);
}
