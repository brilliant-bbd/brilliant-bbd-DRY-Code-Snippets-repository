package com.dry_code_snippets.api.Models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "languages")
@Getter
@Setter
@NoArgsConstructor
public class Language {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "languageId")
    private int languageId;

    @Column(name = "languageName")
    private String languageName;
    public Language(String languageName){
        this.languageName = languageName;
    }
}
