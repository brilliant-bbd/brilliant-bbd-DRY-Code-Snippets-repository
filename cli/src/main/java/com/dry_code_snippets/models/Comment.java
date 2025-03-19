package com.dry_code_snippets.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Comment {

    private static final DateTimeFormatter INPUT_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS");
    private static final DateTimeFormatter OUTPUT_FORMATTER = DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy");

    @JsonProperty("commentId")
    private int commentId;

    @JsonProperty("snippetId")
    private int snippetId;

    @JsonProperty("userId")
    private int userId;

    @JsonProperty("comment")
    private String comment;

    @JsonProperty("createdAt")
    private LocalDateTime createdAt ;

    public Comment() {}

    public Comment(int commentId, int snippetId, String comment, int userId, LocalDateTime createdAt) {
        this.commentId = commentId;
        this.snippetId = snippetId;
        this.userId = userId;
        this.comment = comment;
        this.createdAt = createdAt;
    }

    public int getCommentId() {
        return commentId;
    }
    public void setCommentId(int commentId) {
        this.commentId = commentId;
    }

    public int getSnippetId() {
        return snippetId;
    }
    public void setSnippetId(int snippetId) {
        this.snippetId = snippetId;
    }

    public int getUserId() {
        return userId;
    }
    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getComment() {
        return comment;
    }
    public void setComment(String comment) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode node = objectMapper.readTree(comment);
            this.comment = node.get("comment").asText();
        } catch (Exception e) {
            this.comment = comment;
        }
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(String createdAt) {
        this.createdAt = LocalDateTime.parse(createdAt, INPUT_FORMATTER);
    }

    @Override
    public String toString() {
        return "Date: " +  createdAt.format(OUTPUT_FORMATTER) + "\nComment: " + comment + "\n";
    }
}
