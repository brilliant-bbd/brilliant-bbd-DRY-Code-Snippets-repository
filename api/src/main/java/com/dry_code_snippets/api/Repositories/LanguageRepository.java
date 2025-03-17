package com.dry_code_snippets.api.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dry_code_snippets.api.Models.Language;

@Repository
public interface LanguageRepository extends JpaRepository<Language,Integer>
{
    Language findByLanguageName(String language);
}