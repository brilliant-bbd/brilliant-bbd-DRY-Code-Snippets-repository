package com.dry_code_snippets.api.Repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dry_code_snippets.api.Models.Version;

public interface VersionRepository extends JpaRepository<Version,Long> {

    List<Version> findBySnippetId(Long snippetId);}
