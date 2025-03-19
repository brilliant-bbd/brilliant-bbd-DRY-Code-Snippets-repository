package com.dry_code_snippets.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Version {

    private static final DateTimeFormatter INPUT_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
    private static final DateTimeFormatter OUTPUT_FORMATTER = DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy");

    @JsonProperty("versionId")
    private int versionId;

    @JsonProperty("snippetId")
    private int snippetId;

    @JsonProperty("version")
    private int version;

    @JsonProperty("code")
    private String code;

    @JsonProperty("createdAt")
    private LocalDateTime createdAt;

    public Version() {}

    public Version(int versionId, int snippetId, int version, String code, LocalDateTime createdAt) {
        this.versionId = versionId;
        this.snippetId = snippetId;
        this.version = version;
        this.code = code;
        this.createdAt = createdAt;
    }

    public int getVersionId() {
        return versionId;
    }
    public void setVersionId(int versionId) {
        this.versionId = versionId;
    }

    public int getSnippetId() {
        return snippetId;
    }
    public void setSnippetId(int snippetId) {
        this.snippetId = snippetId;
    }

    public int getVersion() {
        return version;
    }
    public void setVersion(int version) {
        this.version = version;
    }

    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(String createdAt) {
        this.createdAt = LocalDateTime.parse(createdAt, INPUT_FORMATTER);
    }

    @Override
    public String toString() {
        return "Version " + version + " of snippet " + snippetId + "\n"
                + "Created: " + createdAt.format(OUTPUT_FORMATTER) + "\n\n"
                + "Code: \n\n" + code + "\n";


    }
}
