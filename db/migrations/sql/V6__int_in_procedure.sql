
CREATE OR REPLACE VIEW view_snippets AS
SELECT 
    s.snippet_id,
    s.title,
    l.language_name,
    v.latest_version,
    COALESCE(ROUND(AVG(r.rating), 2), 0) AS average_rating
FROM snippets s
JOIN languages l ON s.language_id = l.language_id
LEFT JOIN ratings r ON s.snippet_id = r.snippet_id
LEFT JOIN LATERAL (
    SELECT v.version
    FROM versions v
    WHERE v.snippet_id = s.snippet_id
    ORDER BY v.version DESC
    LIMIT 1
) v ON TRUE
WHERE s.is_deleted = FALSE
GROUP BY s.snippet_id, s.title, l.language_name, v.latest_version;


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
LEFT JOIN LATERAL (
    SELECT v.version, v.code
    FROM versions v
    WHERE v.snippet_id = s.snippet_id
    ORDER BY v.version DESC
    LIMIT 1
) v ON TRUE
WHERE s.is_deleted = FALSE
GROUP BY s.snippet_id, s.title, s.description, l.language_name, v.version, v.code;


CREATE OR REPLACE VIEW view_snippet_versions_by_id AS
SELECT 
    v.version_id,
    v.snippet_id,
    v.version
FROM versions v
ORDER BY v.snippet_id, v.version DESC;


CREATE OR REPLACE VIEW view_version_by_id AS
SELECT 
    v.version_id,
    v.snippet_id,
    v.version,
    v.code
FROM versions v
ORDER BY v.snippet_id, v.version DESC;


CREATE OR REPLACE VIEW view_comments_by_snippet AS
SELECT 
    comment_id,
    snippet_id,
    user_id,
    comment,
    created_at
FROM comments
ORDER BY created_at DESC;



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
    v_initial_version BIGINT;
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


CREATE OR REPLACE PROCEDURE add_version(
    IN p_snippet_id BIGINT,
    IN p_code VARCHAR(10000)
)
LANGUAGE plpgsql
AS $$
DECLARE
    v_latest_version BIGINT;
BEGIN
    LOCK TABLE versions IN SHARE ROW EXCLUSIVE MODE;

    SELECT COALESCE(MAX(version), 0) + 1 INTO v_latest_version
    FROM versions 
    WHERE snippet_id = p_snippet_id
    FOR UPDATE;

    INSERT INTO versions (snippet_id, version, code)
    VALUES (p_snippet_id, v_latest_version, p_code);
END;
$$;

CREATE OR REPLACE PROCEDURE rate_snippet(
    IN p_user_guid UUID,
    IN p_snippet_id BIGINT,
    IN p_rating INT
)
LANGUAGE plpgsql
AS $$
DECLARE
    v_user_id BIGINT;
BEGIN
    SELECT user_id INTO v_user_id FROM users WHERE user_guid = p_user_guid;
    
    IF v_user_id IS NULL THEN
        RAISE EXCEPTION 'User with GUID % not found', p_user_guid;
    END IF;

    IF p_rating < 1 OR p_rating > 5 THEN
        RAISE EXCEPTION 'Rating must be between 1 and 5';
    END IF;

    INSERT INTO ratings (user_id, snippet_id, rating)
    VALUES (v_user_id, p_snippet_id, p_rating)
    ON CONFLICT (user_id, snippet_id)
    DO UPDATE SET rating = EXCLUDED.rating;
END;
$$;

CREATE OR REPLACE PROCEDURE add_comment(
    IN p_user_guid UUID,
    IN p_snippet_id BIGINT,
    IN p_comment VARCHAR(256)
)
LANGUAGE plpgsql
AS $$
DECLARE
    v_user_id BIGINT;
BEGIN
    SELECT user_id INTO v_user_id FROM users WHERE user_guid = p_user_guid;
    
    IF v_user_id IS NULL THEN
        RAISE EXCEPTION 'User with GUID % not found', p_user_guid;
    END IF;

    INSERT INTO comments (user_id, snippet_id, comment)
    VALUES (v_user_id, p_snippet_id, p_comment);
END;
$$;

CREATE OR REPLACE PROCEDURE add_snippet_tag(
    IN p_snippet_id BIGINT,
    IN p_tag_names TEXT[]
)
LANGUAGE plpgsql
AS $$
DECLARE
    v_tag_id BIGINT;
    v_tag_name TEXT;
BEGIN
    FOREACH v_tag_name IN ARRAY p_tag_names
    LOOP
        SELECT tag_id INTO v_tag_id FROM tags WHERE tag_name = v_tag_name;

        IF v_tag_id IS NULL THEN
            INSERT INTO tags (tag_name) VALUES (v_tag_name)
            RETURNING tag_id INTO v_tag_id;
        END IF;

        INSERT INTO snippettags (snippet_id, tag_id)
        VALUES (p_snippet_id, v_tag_id)
        ON CONFLICT DO NOTHING;
    END LOOP;
END;
$$;

CREATE OR REPLACE PROCEDURE soft_delete_snippet(
    IN p_snippet_id BIGINT
)
LANGUAGE plpgsql
AS $$
BEGIN
    UPDATE snippets SET is_deleted = TRUE WHERE snippet_id = p_snippet_id;
END;
$$;

