package com.dry_code_snippets.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Snippet {

    private static final DateTimeFormatter INPUT_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final DateTimeFormatter OUTPUT_FORMATTER = DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy");

    @JsonProperty("snippetId")
    private int snippetId;

    @JsonProperty("userId")
    private int userId;

    @JsonProperty("title")
    private String title;

    @JsonProperty("description")
    private String description;

    @JsonProperty("language")
    private String language;

    @JsonProperty("code")
    private String code;

    @JsonProperty("rating")
    private double rating;

    @JsonProperty("tags")
    private String[] tags;

    @JsonProperty("updatedat")
    private LocalDateTime updatedAt;

    public Snippet() {}

    public Snippet(int snippetId, int userId, String title, String description,
                   String language, String code, double rating, String[] tags, LocalDateTime updatedAt) {
        this.snippetId = snippetId;
        this.userId = userId;
        this.title = title;
        this.description = description;
        this.language = language;
        this.code = code;
        this.rating = rating;
        this.tags = tags;
        this.updatedAt = updatedAt;
    }

    public int getSnippetId() { return snippetId; }
    public void setSnippetId(int snippetId) { this.snippetId = snippetId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getLanguage() { return language; }
    public void setLanguage(String language) { this.language = language; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public double getRating() { return rating; }
    public void setRating(double rating) { this.rating = rating; }

    private String getActualRating() {
        if (rating == -1) {
            return "Unrated";
        } else {
            return "Rated " + String.format("%.2f", rating) + "/10";
        }
    }

    public String[] getTags() { return tags; }
    public void setTags(String[] tags) { this.tags = tags; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(String updatedAt) { this.updatedAt = LocalDateTime.parse(updatedAt, INPUT_FORMATTER); }

    public String getSimpleSnippet() {
        return "Snippet ID: " + snippetId + " - (" + getActualRating() + ")\n"
                + "Title: " + title + "\n"
                + "Language: " + language + "\n"
                + "Tags: " + String.join(", ", tags) + "\n"
                + "Last modified: " + updatedAt.format(OUTPUT_FORMATTER) + "\n";
    }

    public String getSingleSnippet() {
        return  "Title: " + title + " - (" + getActualRating() + ")\n"
                + "Language: " + language + "\n"
                + "Tags: " + String.join(", ", tags) + "\n"
                + "Last modified: " + updatedAt.format(OUTPUT_FORMATTER) + "\n\n"
                + "Description: \n\n" + description + "\n\n"
                + "Code: \n\n" + code + "\n";
    }

}
