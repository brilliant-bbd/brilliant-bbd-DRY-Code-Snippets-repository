package com.dry_code_snippets.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Snippet {

    @JsonProperty("snippetId")
    private int snippetId;

    @JsonProperty("userId")
    private int userId;

    @JsonProperty("title")
    private String title;

    @JsonProperty("description")
    private String description;

    @JsonProperty("languageId")
    private int languageId;

    @JsonProperty("deleted")
    private boolean deleted;

    public Snippet() {}

    public Snippet(int snippetId, int userId, String title, String description, int languageId, boolean deleted) {
        this.snippetId = snippetId;
        this.userId = userId;
        this.title = title;
        this.description = description;
        this.languageId = languageId;
        this.deleted = deleted;
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

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public int getLanguageId() {
        return languageId;
    }
    public void setLanguageId(int languageId) {
        this.languageId = languageId;
    }

    public boolean isDeleted() {
        return deleted;
    }
    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    @Override
    public String toString() {
        return "";
    }
}
