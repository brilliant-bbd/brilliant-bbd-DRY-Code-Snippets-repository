-- Step 1: Drop dependent views before modifying the column
DROP VIEW IF EXISTS view_snippets;
DROP VIEW IF EXISTS view_snippet_by_id;
DROP VIEW IF EXISTS view_snippet_versions_by_id;
DROP VIEW IF EXISTS view_version_by_id;

-- Step 2: Alter versions table to change "version" to INT
ALTER TABLE versions 
ALTER COLUMN version TYPE INT USING version::INTEGER;

-- Step 3: Modify procedures and ensure default version starts at 1

-- Drop existing procedures before recreating them
DROP PROCEDURE IF EXISTS create_snippet;
DROP PROCEDURE IF EXISTS add_version;

-- Recreate create_snippet with default version as 1
CREATE OR REPLACE PROCEDURE create_snippet(
    OUT p_snippet_id INT,
    IN p_user_guid UUID,
    IN p_title VARCHAR(256),
    IN p_description VARCHAR(1000),
    IN p_language_name VARCHAR(256),
    IN p_code VARCHAR(10000)
)
LANGUAGE plpgsql
AS $$
DECLARE
    v_user_id INT;
    v_language_id INT;
BEGIN
    -- Retrieve user_id from user_guid
    SELECT user_id INTO v_user_id FROM users WHERE user_guid = p_user_guid;
    
    -- If user does not exist, raise an error
    IF v_user_id IS NULL THEN
        RAISE EXCEPTION 'User with GUID % not found', p_user_guid;
    END IF;

    -- Check if the language already exists
    SELECT language_id INTO v_language_id 
    FROM languages 
    WHERE language_name = p_language_name;

    -- If language does not exist, create it
    IF v_language_id IS NULL THEN
        INSERT INTO languages (language_name) 
        VALUES (p_language_name)
        RETURNING language_id INTO v_language_id;
    END IF;

    -- Insert the new snippet
    INSERT INTO snippets (user_id, title, description, language_id)
    VALUES (v_user_id, p_title, p_description, v_language_id)
    RETURNING snippet_id INTO p_snippet_id;

    -- Insert the initial version (default to 1)
    INSERT INTO versions (snippet_id, version, code)
    VALUES (p_snippet_id, 1, p_code);
END;
$$;


-- Recreate add_version procedure to increment version numbers
CREATE OR REPLACE PROCEDURE add_version(
    IN p_snippet_id INT,
    IN p_code VARCHAR(10000)
)
LANGUAGE plpgsql
AS $$
DECLARE
    v_latest_version INT;
BEGIN
    -- Get the latest version for the snippet
    SELECT COALESCE(MAX(version), 0) + 1 INTO v_latest_version
    FROM versions 
    WHERE snippet_id = p_snippet_id;

    -- Insert new version with incremented version number
    INSERT INTO versions (snippet_id, version, code)
    VALUES (p_snippet_id, v_latest_version, p_code);
END;
$$;


-- Step 4: Recreate Views with INT version references

CREATE OR REPLACE VIEW view_snippets AS
SELECT 
    s.snippet_id,
    s.title,
    l.language_name,
    (SELECT v.version 
     FROM versions v 
     WHERE v.snippet_id = s.snippet_id 
     ORDER BY v.version DESC 
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
WHERE v.version = (
    SELECT MAX(v2.version) 
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

