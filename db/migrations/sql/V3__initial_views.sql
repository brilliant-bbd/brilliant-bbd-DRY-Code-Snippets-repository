
CREATE OR REPLACE VIEW view_snippets AS
SELECT 
    s.snippet_id,
    s.title,
    l.language_name,
    (SELECT v.version 
     FROM versions v 
     WHERE v.snippet_id = s.snippet_id 
     ORDER BY v.created_at DESC 
     LIMIT 1) AS latest_version,
    COALESCE(ROUND(AVG(r.rating), 2), 0) AS average_rating
FROM snippets s
JOIN languages l ON s.language_id = l.language_id
LEFT JOIN ratings r ON s.snippet_id = r.snippet_id
WHERE s.is_deleted = FALSE
GROUP BY s.snippet_id, s.title, l.language_name;


CREATE OR REPLACE VIEW view_snippet_by_id AS
SELECT 
    s.snippet_id,
    s.title,
    s.description,
    l.language_name,
    v.version AS latest_version,
    v.code AS latest_code,
    COALESCE(ROUND(AVG(r.rating), 2), 0) AS average_rating
FROM snippets s
JOIN languages l ON s.language_id = l.language_id
LEFT JOIN ratings r ON s.snippet_id = r.snippet_id
JOIN versions v ON v.snippet_id = s.snippet_id
WHERE v.created_at = (
    SELECT MAX(v2.created_at) 
    FROM versions v2 
    WHERE v2.snippet_id = s.snippet_id
)
AND s.is_deleted = FALSE
GROUP BY s.snippet_id, s.title, s.description, l.language_name, v.version, v.code;


CREATE OR REPLACE VIEW view_snippet_versions_by_id AS
SELECT 
    v.version_id,
    v.snippet_id,
    v.version
FROM versions v;


CREATE OR REPLACE VIEW view_version_by_id AS
SELECT 
    v.version_id,
    v.version,
    v.code
FROM versions v;


CREATE OR REPLACE VIEW view_comments_by_snippet AS
SELECT 
    comment_id,
    snippet_id,
    comment,
    created_at
FROM comments



