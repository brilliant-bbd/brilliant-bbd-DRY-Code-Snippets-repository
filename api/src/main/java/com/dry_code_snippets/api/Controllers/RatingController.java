package com.dry_code_snippets.api.Controllers;

import com.dry_code_snippets.api.Models.Rating;
import com.dry_code_snippets.api.Services.RatingService;
import com.dry_code_snippets.api.Services.SharedService;

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
    private final SharedService sharedService;

    @Autowired
    public RatingController(RatingService ratingService,SharedService sharedService) {
        this.ratingService = ratingService;
        this.sharedService = sharedService;
    }

    @PostMapping
    public ResponseEntity<Rating> addRating(@RequestParam("snippetId") Long snippetId, @RequestBody @Valid Integer rating) {
        if(!sharedService.resourceExists(snippetId))
        {
            return ResponseEntity.notFound().build();
        }
        Rating createdRating = ratingService.addRating(snippetId, rating);
        return new ResponseEntity<>(createdRating, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Rating>> getRatings(@RequestParam("snippetId") Long snippetId) {
        if(!sharedService.resourceExists(snippetId))
        {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(ratingService.getRatingsForSnippet(snippetId));
    }
}
