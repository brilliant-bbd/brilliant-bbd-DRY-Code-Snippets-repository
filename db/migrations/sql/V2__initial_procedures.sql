
CREATE OR REPLACE FUNCTION create_snippet(
    p_user_id INT,
    p_title VARCHAR(256),
    p_description VARCHAR(1000),
    p_language_name VARCHAR(256),
    p_code TEXT,
    p_version VARCHAR(100) DEFAULT '1.0'
) RETURNS INT AS $$
DECLARE
    v_snippet_id INT;
    v_language_id INT;
BEGIN
    -- Check if the language already exists
    SELECT language_id INTO v_language_id 
    FROM languages 
    WHERE language_name = p_language_name;

    -- If language does not exist, create it
    IF v_language_id IS NULL THEN
        INSERT INTO languages (language_name) VALUES (p_language_name)
        RETURNING language_id INTO v_language_id;
    END IF;

    -- Insert the new snippet
    INSERT INTO snippets (user_id, title, description, language_id)
    VALUES (p_user_id, p_title, p_description, v_language_id)
    RETURNING snippet_id INTO v_snippet_id;

    -- Insert the initial version
    INSERT INTO versions (snippet_id, version, code)
    VALUES (v_snippet_id, p_version, p_code);

    RETURN v_snippet_id;
END;
$$ LANGUAGE plpgsql;


CREATE OR REPLACE FUNCTION add_version(
    p_snippet_id INT,
    p_version VARCHAR(100),
    p_code TEXT
) RETURNS VOID AS $$
BEGIN
    INSERT INTO versions (snippet_id, version, code)
    VALUES (p_snippet_id, p_version, p_code);
END;
$$ LANGUAGE plpgsql;


CREATE OR REPLACE FUNCTION rate_snippet(
    p_user_id INT,
    p_snippet_id INT,
    p_rating INT
) RETURNS VOID AS $$
BEGIN
    -- Ensure rating is between 1 and 5
    IF p_rating < 1 OR p_rating > 5 THEN
        RAISE EXCEPTION 'Rating must be between 1 and 5';
    END IF;

    -- Check if a rating already exists
    IF EXISTS (
        SELECT 1 FROM ratings WHERE user_id = p_user_id AND snippet_id = p_snippet_id
    ) THEN
        -- Update existing rating
        UPDATE ratings 
        SET rating = p_rating 
        WHERE user_id = p_user_id AND snippet_id = p_snippet_id;
    ELSE
        -- Insert new rating
        INSERT INTO ratings (user_id, snippet_id, rating)
        VALUES (p_user_id, p_snippet_id, p_rating);
    END IF;
END;
$$ LANGUAGE plpgsql;



CREATE OR REPLACE FUNCTION add_comment(
    p_user_id INT,
    p_snippet_id INT,
    p_comment VARCHAR(256)
) RETURNS VOID AS $$
BEGIN
    INSERT INTO comments (user_id, snippet_id, comment)
    VALUES (p_user_id, p_snippet_id, p_comment);
END;
$$ LANGUAGE plpgsql;


CREATE OR REPLACE FUNCTION add_snippet_tag(
    p_snippet_id INT,
    p_tag_names VARCHAR(256)[]
) RETURNS VOID AS $$
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
$$ LANGUAGE plpgsql;


CREATE OR REPLACE FUNCTION soft_delete_snippet(
    p_snippet_id INT
) RETURNS VOID AS $$
BEGIN
    UPDATE snippets SET is_deleted = TRUE WHERE snippet_id = p_snippet_id;
END;
$$ LANGUAGE plpgsql;


