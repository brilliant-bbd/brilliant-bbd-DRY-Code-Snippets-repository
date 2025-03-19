package com.dry_code_snippets.api.Ratings;

import com.dry_code_snippets.api.Models.Rating;
import com.dry_code_snippets.api.Models.User;
import com.dry_code_snippets.api.Repositories.RatingRepository;
import com.dry_code_snippets.api.Repositories.SnippetRepository;
import com.dry_code_snippets.api.Repositories.UserRepository;
import com.dry_code_snippets.api.Services.RatingService;
import com.dry_code_snippets.api.Shared.Shared;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RatingServiceTest {

    @Mock
    private RatingRepository ratingRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private RatingService ratingService;

    private Optional<User> user;
    private Rating rating;

    @BeforeEach
    void setUp() {

        user = Optional.of(new User());
        user.get().setUserId(101L);
        rating = new Rating(1L, user.get().getUserId(), 5);
    }

    @Test
    void testAddRating() {
        when(userRepository.findByUserGuid(Shared.getClaim())).thenReturn(user);
        when(ratingRepository.save(any(Rating.class))).thenReturn(rating);

        Rating savedRating = ratingService.addRating(1L, 5);

        assertNotNull(savedRating);
        assertEquals(101L, savedRating.getUserId());
        assertEquals(5, savedRating.getRating());
        verify(ratingRepository, times(1)).save(any(Rating.class));
    }

    @Test
    void testGetRatingsForSnippet() {
        when(ratingRepository.findBySnippetId(1L)).thenReturn(List.of(rating));

        List<Rating> ratings = ratingService.getRatingsForSnippet(1L);

        assertEquals(1, ratings.size());
        assertEquals(5, ratings.get(0).getRating());
        verify(ratingRepository, times(1)).findBySnippetId(1L);
    }
}
