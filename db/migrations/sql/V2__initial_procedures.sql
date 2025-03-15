
CREATE OR REPLACE PROCEDURE create_snippet(
    OUT p_snippet_id INT,
    IN p_user_guid UUID,
    IN p_title VARCHAR(256),
    IN p_description VARCHAR(1000),
    IN p_language_name VARCHAR(256),
    IN p_code VARCHAR(10000),
    IN p_version VARCHAR(100)
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

    -- If no version is provided, set default value inside the procedure
    IF p_version IS NULL THEN
        p_version := '1.0';
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

    -- Insert the initial version
    INSERT INTO versions (snippet_id, version, code)
    VALUES (p_snippet_id, p_version, p_code);
END;
$$;


CREATE OR REPLACE PROCEDURE rate_snippet(
    IN p_user_guid UUID,
    IN p_snippet_id INT,
    IN p_rating INT
)
LANGUAGE plpgsql
AS $$
DECLARE
    v_user_id INT;
BEGIN
    -- Retrieve user_id from user_guid
    SELECT user_id INTO v_user_id FROM users WHERE user_guid = p_user_guid;
    
    -- If user does not exist, raise an error
    IF v_user_id IS NULL THEN
        RAISE EXCEPTION 'User with GUID % not found', p_user_guid;
    END IF;

    -- Ensure rating is between 1 and 5
    IF p_rating < 1 OR p_rating > 5 THEN
        RAISE EXCEPTION 'Rating must be between 1 and 5';
    END IF;

    -- Check if a rating already exists
    IF EXISTS (
        SELECT 1 FROM ratings WHERE user_id = v_user_id AND snippet_id = p_snippet_id
    ) THEN
        -- Update existing rating
        UPDATE ratings 
        SET rating = p_rating 
        WHERE user_id = v_user_id AND snippet_id = p_snippet_id;
    ELSE
        -- Insert new rating
        INSERT INTO ratings (user_id, snippet_id, rating)
        VALUES (v_user_id, p_snippet_id, p_rating);
    END IF;
END;
$$;


CREATE OR REPLACE PROCEDURE add_comment(
    IN p_user_guid UUID,
    IN p_snippet_id INT,
    IN p_comment VARCHAR(256)
)
LANGUAGE plpgsql
AS $$
DECLARE
    v_user_id INT;
BEGIN
    -- Retrieve user_id from user_guid
    SELECT user_id INTO v_user_id FROM users WHERE user_guid = p_user_guid;
    
    -- If user does not exist, raise an error
    IF v_user_id IS NULL THEN
        RAISE EXCEPTION 'User with GUID % not found', p_user_guid;
    END IF;

    -- Insert comment
    INSERT INTO comments (user_id, snippet_id, comment)
    VALUES (v_user_id, p_snippet_id, p_comment);
END;
$$;


CREATE OR REPLACE PROCEDURE add_version(
    IN p_snippet_id INT,
    IN p_version VARCHAR(100),
    IN p_code VARCHAR(10000)
)
LANGUAGE plpgsql
AS $$
BEGIN
    INSERT INTO versions (snippet_id, version, code)
    VALUES (p_snippet_id, p_version, p_code);
END;
$$;


CREATE OR REPLACE PROCEDURE add_snippet_tag(
    IN p_snippet_id INT,
    IN p_tag_names VARCHAR(256)[]
)
LANGUAGE plpgsql
AS $$
DECLARE
    v_tag_id INT;
    v_tag_name TEXT;
BEGIN
    -- Loop through each tag name in the input array
    FOREACH v_tag_name IN ARRAY p_tag_names
    LOOP
        -- Check if the tag exists
        SELECT tag_id INTO v_tag_id 
        FROM tags 
        WHERE tag_name = v_tag_name;

        -- If the tag does not exist, insert it
        IF v_tag_id IS NULL THEN
            INSERT INTO tags (tag_name) VALUES (v_tag_name)
            RETURNING tag_id INTO v_tag_id;
        END IF;

        -- Insert into snippettags, avoiding duplicates
        INSERT INTO snippettags (snippet_id, tag_id)
        VALUES (p_snippet_id, v_tag_id)
        ON CONFLICT DO NOTHING;
    END LOOP;
END;
$$;


CREATE OR REPLACE PROCEDURE soft_delete_snippet(
    IN p_snippet_id INT
)
LANGUAGE plpgsql
AS $$
BEGIN
    UPDATE snippets SET is_deleted = TRUE WHERE snippet_id = p_snippet_id;
END;
$$;

