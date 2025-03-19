package com.dry_code_snippets.api.Services;

import com.dry_code_snippets.api.Models.Rating;
import com.dry_code_snippets.api.Models.Snippet;
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
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RatingServiceTest {

    @Mock
    private RatingRepository ratingRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private SnippetRepository snippetRepository;

    @InjectMocks
    private RatingService ratingService;

    private Optional<User> user;
    private Rating rating;

    private User mockUser;
    private Snippet mockSnippet;
    private Rating existingRating;

    @BeforeEach
    void setUp() {

        mockUser = new User(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"));
        mockUser.setUserId(2L);

        mockSnippet = new Snippet(1L, "Test Snippet", "Code", 101l);
        mockSnippet.setUserId(1L);

        existingRating = new Rating(1L, 2L, 4);

        user = Optional.of(new User());
        user.get().setUserId(101L);
        rating = new Rating(2L, user.get().getUserId(), 5);
    }

    @Test
    void testAddRating_NewRating_Success() {
        when(userRepository.findByUserGuid(Shared.getClaim())).thenReturn(Optional.of(mockUser));
        when(snippetRepository.findById(1L)).thenReturn(Optional.of(mockSnippet));
        when(ratingRepository.findBySnippetId(1L)).thenReturn(List.of());
        when(ratingRepository.save(any(Rating.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Rating result = ratingService.addRating(1L, 5);

        assertNotNull(result);
        assertEquals(5, result.getRating());
        assertEquals(mockUser.getUserId(), result.getUserId());
        verify(ratingRepository, times(1)).save(any(Rating.class));
    }

    @Test
    void testAddRating_UpdateExistingRating_Success() {
        when(userRepository.findByUserGuid(Shared.getClaim())).thenReturn(Optional.of(mockUser));
        when(snippetRepository.findById(1L)).thenReturn(Optional.of(mockSnippet));
        when(ratingRepository.findBySnippetId(1L)).thenReturn(List.of(existingRating));
        when(ratingRepository.save(any(Rating.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Rating result = ratingService.addRating(1L, 3);

        assertNotNull(result);
        assertEquals(3, result.getRating());
        assertEquals(mockUser.getUserId(), result.getUserId());
        verify(ratingRepository, times(1)).save(any(Rating.class));
    }

    @Test
    void testAddRating_UsersCannotRateOwnSnippet_ShouldThrowException() {
        mockSnippet.setUserId(mockUser.getUserId());

        when(userRepository.findByUserGuid(Shared.getClaim())).thenReturn(Optional.of(mockUser));
        when(snippetRepository.findById(1L)).thenReturn(Optional.of(mockSnippet));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            ratingService.addRating(1L, 5);
        });

        assertEquals("Users cannot rate their own snippets.", exception.getMessage());
        verify(ratingRepository, never()).save(any(Rating.class));
    }

    @Test
    void testAddRating_SnippetIsDeleted_ShouldThrowException() {
        mockSnippet.setDeleted(true);

        when(userRepository.findByUserGuid(Shared.getClaim())).thenReturn(Optional.of(mockUser));
        when(snippetRepository.findById(1L)).thenReturn(Optional.of(mockSnippet));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            ratingService.addRating(1L, 5);
        });

        assertEquals("Snippet is deleted for ID: 1", exception.getMessage());
        verify(ratingRepository, never()).save(any(Rating.class));
    }

    @Test
    void testAddRating_UserNotFound_ShouldThrowException() {
        when(userRepository.findByUserGuid(Shared.getClaim())).thenReturn(Optional.empty());

        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> {
            ratingService.addRating(1L, 5);
        });

        verify(ratingRepository, never()).save(any(Rating.class));
    }

    @Test
    void testAddRating_SnippetNotFound_ShouldThrowException() {
        when(userRepository.findByUserGuid(Shared.getClaim())).thenReturn(Optional.of(mockUser));
        when(snippetRepository.findById(1L)).thenReturn(Optional.empty());

        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> {
            ratingService.addRating(1L, 5);
        });

        assertEquals("Snippet not found for ID: 1", exception.getMessage());
        verify(ratingRepository, never()).save(any(Rating.class));
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
