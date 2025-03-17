package com.dry_code_snippets.api.Models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "versions")
@Getter
@Setter
@NoArgsConstructor
public class Version {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "versionId")
    private long versionId ;

    @Column(name="snippetId")
    private long snippetId;

    @Column(name="version")
    private String version;

    @Column (name = "code")
    private String code;

    public Version(long snippetId, String version, String code)
    {
        this.snippetId = snippetId;
        this.version = version;
        this.code = code;
    }
}
