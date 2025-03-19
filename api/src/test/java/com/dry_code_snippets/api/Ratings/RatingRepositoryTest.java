package com.dry_code_snippets.api.Ratings;

import com.dry_code_snippets.api.Models.Rating;
import com.dry_code_snippets.api.Repositories.RatingRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class RatingRepositoryTest {

    @Autowired
    private RatingRepository ratingRepository;

    private Rating rating1, rating2;

    @BeforeEach
    void setUp() {
        rating1 = new Rating(1L, 101L, 5);
        rating2 = new Rating(1L, 102L, 4);
        ratingRepository.save(rating1);
        ratingRepository.save(rating2);
    }

    @AfterEach
    void tearDown() {
        ratingRepository.deleteAll();
    }

    @Test
    @Transactional
    @Rollback
    void testFindBySnippetId() {
        List<Rating> ratings = ratingRepository.findRatingsBySnippetId(1L);
        assertEquals(2, ratings.size());
        assertTrue(ratings.stream().anyMatch(r -> r.getUserId().equals(101L)));
        assertTrue(ratings.stream().anyMatch(r -> r.getUserId().equals(102L)));
    }

    @Test
    @Transactional
    @Rollback
    void testSaveRating() {
        Rating newRating = new Rating(2L, 103L, 3);
        Rating savedRating = ratingRepository.save(newRating);

        assertNotNull(savedRating.getRatingId());
        assertEquals(2L, savedRating.getSnippetId());
        assertEquals(103L, savedRating.getUserId());
        assertEquals(3, savedRating.getRating());
    }
}
