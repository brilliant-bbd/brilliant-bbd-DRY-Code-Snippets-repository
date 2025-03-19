package com.dry_code_snippets.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Rating {

    private static final DateTimeFormatter INPUT_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    @JsonProperty("ratingId")
    private int ratingId;

    @JsonProperty("snippetId")
    private int snippetId;

    @JsonProperty("userId")
    private int userId;

    @JsonProperty("rating")
    private int rating;

    @JsonProperty("createdAt")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSS")
    private LocalDateTime createdAt;

    public Rating() {}

    public Rating(int ratingId, int snippetId, int userId, int rating, LocalDateTime createdAt) {
        this.ratingId = ratingId;
        this.snippetId = snippetId;
        this.userId = userId;
        this.rating = rating;
        this.createdAt = createdAt;
    }

    public int getRatingId() { return ratingId; }
    public void setRatingId(int ratingId) { this.ratingId = ratingId; }

    public int getSnippetId() { return snippetId; }
    public void setSnippetId(int snippetId) { this.snippetId = snippetId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public int getRating() { return rating; }
    public void setRating(int rating) { this.rating = rating; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = LocalDateTime.parse(createdAt, INPUT_FORMATTER); }
}

