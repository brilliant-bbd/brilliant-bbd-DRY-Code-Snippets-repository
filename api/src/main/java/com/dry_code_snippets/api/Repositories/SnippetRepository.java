package com.dry_code_snippets.api.Repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.dry_code_snippets.DTO.SnippetDTO;
import com.dry_code_snippets.api.Models.Snippet;

@Repository
public interface SnippetRepository extends JpaRepository<Snippet, Long> {

    Snippet findSnippetByDescription(String description);

    @Query(value = """
            SELECT
                s.snippet_id,
                s.user_id,
                s.title,
                s.description,
                l.language_name AS language,
                v.code,
                v.created_at AS updatedAt,
                AVG(r.rating) AS rating,
                string_agg(t.tag_name, ',') as tags
            FROM
                snippets s
            JOIN
                SnippetTags st ON st.snippet_id = s.snippet_id
            JOIN
                tags t ON t.tag_id = st.tag_id
            JOIN
                versions v ON v.snippet_id = s.snippet_id
            JOIN
                languages l ON s.language_id = l.language_id
            LEFT JOIN
                ratings r ON r.snippet_id = s.snippet_id
            WHERE
                s.is_deleted = FALSE
                AND t.tag_name IN (:tags)
                AND l.language_name = :language
                AND v.created_at = (SELECT MAX(v2.created_at) FROM versions v2 WHERE v2.snippet_id = s.snippet_id)
            GROUP BY
                s.snippet_id, s.title, s.description, l.language_name, v.code, v.created_at
            """, nativeQuery = true)
    List<SnippetDTO> findSnippetsWithTagsAndRatings(
           @Param("language") String language,
            @Param("tags") List<String> tags
           );

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
