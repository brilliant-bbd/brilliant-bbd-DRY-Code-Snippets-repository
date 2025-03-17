package com.dry_code_snippets.api.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dry_code_snippets.api.Models.SnippetTag;

public interface SnippetTagRepository extends JpaRepository<SnippetTag,Long>{
    
    
}