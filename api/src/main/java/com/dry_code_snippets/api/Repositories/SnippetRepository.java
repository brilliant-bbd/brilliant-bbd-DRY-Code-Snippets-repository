package com.dry_code_snippets.api.Repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.dry_code_snippets.api.DTO.SnippetDTO;
import com.dry_code_snippets.api.Models.Snippet;

@Repository
public interface SnippetRepository extends JpaRepository<Snippet, Long> {

    Snippet findSnippetByDescription(String description);

    String BASE_QUERY = """
            SELECT
                s.snippet_id,
                s.user_id,
                s.title,
                s.description,
                l.language_name AS language,
                TO_CHAR((v.created_at AT TIME ZONE 'UTC' AT TIME ZONE 'SAST'), 'YYYY-MM-DD HH24:MI:SS') AS "updatedAt",
                v.code AS "code",
                COALESCE(AVG(r.rating), -1) AS rating,
                ARRAY_AGG(t.tag_name) AS tags
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
                AND v.created_at = (SELECT MAX(v2.created_at) FROM versions v2 WHERE v2.snippet_id = s.snippet_id)
            """;

    String GROUP_BY_CLAUSE = """
            GROUP BY
                s.snippet_id, s.title, s.description, l.language_name, v.code, v.created_at
            ORDER BY
                s.snippet_id DESC;
            """;

    @Query(value = BASE_QUERY + """
            AND (:tags IS NULL OR t.tag_name IN (:tags))
            AND (:language IS NULL OR l.language_name = :language)
            """ + GROUP_BY_CLAUSE, nativeQuery = true)
    List<SnippetDTO> findSnippetsWithTagsAndLanguage(
            @Param("language") String language,
            @Param("tags") List<String> tags);

    @Query(value = BASE_QUERY + """
            AND l.language_name = :language
            """ + GROUP_BY_CLAUSE, nativeQuery = true)
    List<SnippetDTO> findSnippetsWithNullTagsAndLanguage(@Param("language") String language);

    @Query(value = BASE_QUERY + """
            AND t.tag_name IN :tags
            """ + GROUP_BY_CLAUSE, nativeQuery = true)
    List<SnippetDTO> findSnippetsWithTagsAndLanguageAndNullLanguage(@Param("tags") List<String> tagAndNull);

    @Query(value = BASE_QUERY + GROUP_BY_CLAUSE, nativeQuery = true)
    List<SnippetDTO> findSnippetsWithNullTagsAndNullLanguage();

    @Query(value = BASE_QUERY + """
            AND s.snippet_id = :id
            """ + GROUP_BY_CLAUSE, nativeQuery = true)
    Optional<SnippetDTO> findSnippetDtoById(@Param("id") Long id);

    default Long softDeleteById(Long id) {
        Optional<Snippet> snippet = findById(id);
        if (snippet.isPresent()) {
            Snippet foundSnippet = snippet.get();
            foundSnippet.setDeleted(true);
            save(foundSnippet);
            return id;
        }
        return null;
    }
}