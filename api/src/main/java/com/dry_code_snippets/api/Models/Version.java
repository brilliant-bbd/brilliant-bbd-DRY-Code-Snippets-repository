package com.dry_code_snippets.api.Models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "versions")
@Getter
@Setter
@NoArgsConstructor
public class Version {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "versionId")
    private Long versionId;

    @Column(name = "snippetId", nullable = false)
    private Long snippetId;

    @Column(name = "version", nullable = false)
    private int version;

    @Column(name = "code", nullable = false)
    private String code;

    @Column(name = "createdAt")
    private LocalDateTime createdAt;

    public Version(Long snippetId, int version, String code) {
        this.snippetId = snippetId;
        this.version = version;
        this.code = code;
        this.createdAt = LocalDateTime.now();
    }


}