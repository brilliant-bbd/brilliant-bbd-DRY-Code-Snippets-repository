ALTER TABLE ratings ALTER COLUMN rating SET DATA TYPE BIGINT;

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
FROM comments;



CREATE OR REPLACE PROCEDURE create_snippet(
    OUT p_snippet_id BIGINT,
    IN p_user_guid UUID,
    IN p_title VARCHAR(256),
    IN p_description VARCHAR(1000),
    IN p_language_name VARCHAR(256),
    IN p_code VARCHAR(10000)
)
LANGUAGE plpgsql
AS $$
DECLARE
    v_user_id BIGINT;
    v_language_id BIGINT;
BEGIN
    SELECT user_id INTO v_user_id FROM users WHERE user_guid = p_user_guid;
    
    IF v_user_id IS NULL THEN
        RAISE EXCEPTION 'User with GUID % not found', p_user_guid;
    END IF;

    SELECT language_id INTO v_language_id FROM languages WHERE language_name = p_language_name;

    IF v_language_id IS NULL THEN
        INSERT INTO languages (language_name) 
        VALUES (p_language_name)
        RETURNING language_id INTO v_language_id;
    END IF;

    INSERT INTO snippets (user_id, title, description, language_id)
    VALUES (v_user_id, p_title, p_description, v_language_id)
    RETURNING snippet_id INTO p_snippet_id;

    INSERT INTO versions (snippet_id, version, code)
    VALUES (p_snippet_id, 1, p_code);
END;
$$;


