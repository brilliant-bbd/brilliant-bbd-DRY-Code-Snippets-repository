package com.dry_code_snippets.api.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import com.dry_code_snippets.DTO.SnippetDTO;
import com.dry_code_snippets.api.Models.*;
import com.dry_code_snippets.api.Repositories.*;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class SnippetService {

    private final SnippetRepository snippetRepository;
    private final UserRepository userRepository;
    private final VersionRepository versionRepository;
    private final LanguageRepository languageRepository;
    private final UserService userService;
    private final TagRepository tagRepository;
    private final SnippetTagRepository snippetTagRepository;

    @Autowired
    public SnippetService(SnippetRepository snippetRepository, UserRepository userRepository,
            VersionRepository versionRepository, LanguageRepository languageRepository,
            UserService userService, TagRepository tagRepository, SnippetTagRepository snippetTagRepository) {
        this.snippetRepository = snippetRepository;
        this.userRepository = userRepository;
        this.versionRepository = versionRepository;
        this.languageRepository = languageRepository;
        this.userService = userService;
        this.tagRepository = tagRepository;
        this.snippetTagRepository = snippetTagRepository;
    }

    public List<SnippetDTO> getAllSnippets(Optional<String> tag, Optional<String> language) {
        String tags = tag.orElse(null);
        String programmingLanguage = language.orElse(null);
        List<String> tagList = Arrays.asList(tags.split(";"));
        if (tags.isEmpty() && programmingLanguage.isEmpty()) {
            return snippetRepository.findSnippetsWithNullTagsAndNullLanguage();
        } else if (tags.isEmpty() && !programmingLanguage.isEmpty()) {
            return snippetRepository.findSnippetsWithNullTagsAndLanguage(programmingLanguage);
        } else if (!tags.isEmpty() && programmingLanguage.isEmpty()) {
            return snippetRepository.findSnippetsWithTagsAndLanguageAndNullLanguage(tagList);
        } else {
            return snippetRepository.findSnippetsWithTagsAndLanguage(programmingLanguage, tagList);
        }
    }

    public Optional<SnippetDTO> getSnippetById(Long snippetId) {
        return snippetRepository.findSnippetDtoById(snippetId);
    }

    @Transactional
    public Snippet createSnippet(SnippetDTO snippetDTO) {
        try {
            Language language = languageRepository.findByLanguageName(snippetDTO.getLanguage())
                    .orElseGet(() -> languageRepository.save(new Language(snippetDTO.getLanguage())));

            User user = userRepository.findByUserGuid(userService.getClaim())
                    .orElseGet(() -> userRepository.save(new User(userService.getClaim())));

            Snippet snippet = new Snippet(user.getUserId(), snippetDTO.getTitle(), snippetDTO.getDescription(),
                    language.getLanguageId());
            Snippet savedSnippet = snippetRepository.save(snippet);

            List<String> snippetTagList = List.of(snippetDTO.getTags());
            snippetTagList.forEach((tag) -> {
                Tag savedTag = tagRepository.save(new Tag(tag));
                snippetTagRepository.save(new SnippetTag(savedSnippet.getSnippetId(), savedTag.getTagId()));
            });

            Version version = new Version(savedSnippet.getSnippetId(), 1L, snippetDTO.getCode());
            versionRepository.save(version);

            return savedSnippet;
        } catch (DataAccessException e) {
            System.err.println("Error creating snippet: " + e.getMessage());
            throw new RuntimeException("Failed to create snippet", e);
        }
    }

    @Transactional
    public Snippet updateSnippet(Long snippetId, String newCode) {
        Snippet existingSnippet = snippetRepository.findById(snippetId)
                .orElseThrow(() -> new IllegalArgumentException("Snippet not found"));

        User user = userRepository.findByUserGuid(userService.getClaim())
                .orElseThrow(() -> new IllegalStateException("User not found"));

        if (!existingSnippet.getUserId().equals(user.getUserId())) {
            throw new IllegalArgumentException("You do not have permission to update this snippet");
        }

        Version nextVersionNumber = versionRepository.findLatestVersionBySnippetId(snippetId)
                .orElse(new Version(snippetId, 0L, newCode));

        Version version = new Version(existingSnippet.getSnippetId(), nextVersionNumber.getVersion() + 1, newCode);
        versionRepository.save(version);

        return existingSnippet;
    }

    public Long deleteSnippet(@NotNull Long snippetId) {
        return snippetRepository.softDeleteById(snippetId);
    }
}