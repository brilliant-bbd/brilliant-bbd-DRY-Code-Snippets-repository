package com.dry_code_snippets.api.Ratings;


import com.dry_code_snippets.api.Models.Rating;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class RatingTest {
    @Test
    public void testSnippetRecordCreation() {
        Rating rating = new Rating(1L,2L,5);

        assertNotNull(rating);
        assertEquals(1L, rating.getSnippetId());
        assertEquals(2L, rating.getUserId());
        assertEquals(5, rating.getRating());
    }
}
