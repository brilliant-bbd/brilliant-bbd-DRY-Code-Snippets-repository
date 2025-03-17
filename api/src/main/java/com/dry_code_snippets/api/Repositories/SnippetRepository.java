package com.dry_code_snippets.api.Repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.dry_code_snippets.api.Models.Snippet;

@Repository
public interface SnippetRepository extends JpaRepository<Snippet, Long> {

    Snippet findSnippetByDescription(String description);

    @Query(value="SELECT s.* FROM snippets s JOIN SnippetTags st ON st.snippet_id = s.snippet_id JOIN tags t ON t.tag_id = st.tag_id JOIN versions v on v.snippet_id = s.snippet_id JOIN languages l ON s.language_id = l.language_id WHERE t.tag_name IN %:tags% AND s.is_deleted = false AND v.created_at = (SELECT MAX(v2.created_at) FROM versions v2 WHERE v2.snippet_id = s.snippet_id) AND l.language_name = :language",nativeQuery = true)
    List<Snippet> findByLanguageAndTagsAndNotisDeleted(String language, List<String> tags);

    @Override
    default void deleteById(Long id) {
        Optional<Snippet> snippet = findById(id);
        if (snippet.isPresent()) {
            Snippet foundSnippet = snippet.get();
            foundSnippet.setDeleted(true);
            save(foundSnippet);
        }
    }

    // List<Snippet> findByTagsLanguageAll();

    // List<Snippet> findByTagsAndNotisDeleted(List<String> tags);

    // List<Snippet> findByLanguageAndNotisDeleted(String languageId);
}

