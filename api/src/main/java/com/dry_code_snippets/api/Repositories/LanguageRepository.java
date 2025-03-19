package com.dry_code_snippets.api.Repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dry_code_snippets.api.Models.Language;

@Repository
public interface LanguageRepository extends JpaRepository<Language,Integer>
{
    Optional<Language> findByLanguageName(String languageName);
}