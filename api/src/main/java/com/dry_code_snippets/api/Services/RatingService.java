package com.dry_code_snippets.api.Services;

import com.dry_code_snippets.api.Models.User;
import com.dry_code_snippets.api.Repositories.UserRepository;
import com.dry_code_snippets.api.Shared.Shared;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import com.dry_code_snippets.api.Models.Rating;
import com.dry_code_snippets.api.Models.Snippet;
import com.dry_code_snippets.api.Repositories.RatingRepository;
import com.dry_code_snippets.api.Repositories.SnippetRepository;

import java.util.List;
import java.util.NoSuchElementException;
import java.time.LocalDateTime;

@Service
public class RatingService {

    private final RatingRepository ratingRepository;

    private final SnippetRepository snippetRepository;

    private final UserRepository userRepository;

    @Autowired
    public RatingService(RatingRepository ratingRepository, SnippetRepository snippetRepository, UserRepository userRepository) {
        this.ratingRepository = ratingRepository;
        this.snippetRepository = snippetRepository;
        this.userRepository = userRepository;
    }

    public Rating addRating(Long snippetId, Integer ratingNum) {
        User user = userRepository.findByUserGuid(Shared.getClaim()).orElseThrow();

        Snippet snippet = snippetRepository.findById(snippetId)
                .orElseThrow(() -> new NoSuchElementException("Snippet not found for ID: " + snippetId));

        if (snippet.getUserId().equals(user.getUserId())) {
            throw new IllegalArgumentException("Users cannot rate their own snippets.");
        }

        if (snippet.isDeleted()) {
            throw new IllegalArgumentException("Snippet is deleted for ID: " + snippetId);
        }

        List<Rating> allRatings = ratingRepository.findRatingsBySnippetId(snippetId);

        Rating rating = null;
        for (Rating rate : allRatings) {
            if (rate.getUserId() == user.getUserId()) {
                rating = rate;
                break;
            }
        }

        if (rating == null) {
            rating = new Rating(snippetId,user.getUserId(),ratingNum);
        } else {
            rating.setRating(ratingNum);
            rating.setCreatedAt(LocalDateTime.now());
        }

        return ratingRepository.save(rating);
    }

    public List<Rating> getRatingsForSnippet(Long snippetId) {
        return ratingRepository.findRatingsBySnippetId(snippetId);
    }
}
