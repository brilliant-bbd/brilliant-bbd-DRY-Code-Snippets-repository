package com.dry_code_snippets.api.Services;

import com.dry_code_snippets.api.Models.User;
import com.dry_code_snippets.api.Repositories.UserRepository;
import com.dry_code_snippets.api.Shared.Shared;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import com.dry_code_snippets.api.Models.Rating;
import com.dry_code_snippets.api.Repositories.RatingRepository;
import java.util.List;

@Service
public class RatingService {

    private final RatingRepository ratingRepository;

    private final UserRepository userRepository;

    @Autowired
    public RatingService(RatingRepository ratingRepository, UserRepository userRepository) {
        this.ratingRepository = ratingRepository;
        this.userRepository = userRepository;
    }

    public Rating addRating(Long snippetId, Integer ratingNum) {
        User user = userRepository.findByUserGuid(Shared.getClaim());
        Rating rating= new Rating(snippetId,user.getUserId(),ratingNum);
        return ratingRepository.save(rating);
    }

    public List<Rating> getRatingsForSnippet(Long snippetId) {
        return ratingRepository.findBySnippetId(snippetId);
    }
}
