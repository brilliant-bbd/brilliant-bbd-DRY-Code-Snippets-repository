package com.dry_code_snippets.api.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import com.dry_code_snippets.DTO.SnippetDTO;
import com.dry_code_snippets.api.Models.*;
import com.dry_code_snippets.api.Repositories.*;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
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
        String tagAndNull = tag.orElse(null);
        String languageOrNull = language.orElse(null);
        if(tagAndNull.isEmpty())
        {
            return snippetRepository.findSnippetsWithTagsAndRatingsAndNullTags(languageOrNull);
        }
        if(languageOrNull.isEmpty())
        {
            return snippetRepository.findSnippetsWithTagsAndRatingsAndNullLanguage(List.of(tagAndNull.split(";")));
        }

        return snippetRepository.findSnippetsWithTagsAndRatings(languageOrNull,List.of(tagAndNull.split(";")));
    }

    public Optional<Snippet> getSnippetById(Long snippetId) {
        return snippetRepository.findById(snippetId);
    }

    @Transactional
    public Snippet createSnippet(SnippetDTO snippetDTO) {
        try {
            Language language = languageRepository.findByLanguageName(snippetDTO.getLanguage())
                    .orElseGet(() -> languageRepository.save(new Language(snippetDTO.getLanguage())));

            User user = userRepository.findByUserGuid(userService.getClaim())
                    .orElseGet(() -> userRepository.save(new User(userService.getClaim())));

            Tag tag = new Tag(snippetDTO.getTitle());
            tag = tagRepository.save(tag);

            Snippet snippet = new Snippet(user.getUserId(), snippetDTO.getTitle(), snippetDTO.getDescription(),
                    language.getLanguageId());
            Snippet savedSnippet = snippetRepository.save(snippet);

            snippetTagRepository.save(new SnippetTag(savedSnippet.getSnippetId(), tag.getTagId()));

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

    public void deleteSnippet(@NotNull Long snippetId) {
        snippetRepository.deleteById(snippetId);
    }
}