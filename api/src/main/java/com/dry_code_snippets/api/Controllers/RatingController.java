package com.dry_code_snippets.api.Controllers;

import com.dry_code_snippets.api.Models.Rating;
import com.dry_code_snippets.api.Services.RatingService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/snippets/ratings")
public class RatingController {
    private final RatingService ratingService;

    @Autowired
    public RatingController(RatingService ratingService) {
        this.ratingService = ratingService;
    }

    @PostMapping
    public ResponseEntity<Rating> addRating(@PathVariable("id") Long id, @RequestBody @Valid Integer rating) {
        Rating createdRating = ratingService.addRating(id, rating);
        return new ResponseEntity<>(createdRating, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Rating>> getRatings(@RequestParam("snippetId") Long snippetId) {
        return ResponseEntity.ok(ratingService.getRatingsForSnippet(snippetId));
    }
}
