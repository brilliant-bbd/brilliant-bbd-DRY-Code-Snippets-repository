package com.dry_code_snippets.api.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.dry_code_snippets.DTO.SnippetDTO;
import com.dry_code_snippets.api.Models.Language;
import com.dry_code_snippets.api.Models.Snippet;
import com.dry_code_snippets.api.Models.User;
import com.dry_code_snippets.api.Models.Version;
import com.dry_code_snippets.api.Repositories.LanguageRepository;
import com.dry_code_snippets.api.Repositories.SnippetRepository;
import com.dry_code_snippets.api.Repositories.UserRepository;
import com.dry_code_snippets.api.Repositories.VersionRepository;

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
    @Autowired
    public SnippetService(SnippetRepository snippetRepository, UserRepository userRepository,
            VersionRepository versionRepository, LanguageRepository languageRepository,
            UserService userService) {
        this.snippetRepository = snippetRepository;
        this.userRepository = userRepository;
        this.versionRepository = versionRepository;
        this.languageRepository = languageRepository;
        this.userService=userService;
    }

    

    public List<Snippet> getAllSnippets(String tag, String language) {
        if (tag != null && !tag.isEmpty() && language != null && !language.isEmpty()) {
            List<String> tags = Arrays.asList(tag.split(";"));
            return snippetRepository.findByLanguageAndTagsAndNotisDeleted(language,tags);
        }
        return snippetRepository.findAll();
    }

    public Optional<Snippet> getSnippetById(Long snippetId) {
        return snippetRepository.findById(snippetId);
    }

    @Transactional
    public Snippet createSnippet(SnippetDTO snippetDTO) {
        Language language = languageRepository.findByLanguageName(snippetDTO.getLanguage());
        if (language == null) {
            throw new IllegalArgumentException("Language not found");
        }

        User user = userRepository.findByUserGuid(userService.getClaim());
        if (user == null) {
            user = new User(userService.getClaim());
            user = userRepository.save(user);
        }

        Snippet snippet = new Snippet(user.getUserId(), snippetDTO.getTitle(), snippetDTO.getDescription(),
                language.getLanguageId());
        Snippet savedSnippet = snippetRepository.save(snippet);

        Version version = new Version(savedSnippet.getSnippetId(), 1, snippetDTO.getCode());
        versionRepository.save(version);

        return savedSnippet;
    }

    @Transactional
    public Snippet updateSnippet(Long snippetId, SnippetDTO snippetDTO) {
    Snippet existingSnippet = snippetRepository.findById(snippetId).orElse(null);
    
    if (existingSnippet == null) {
        throw new IllegalArgumentException("Snippet not found");
    }

    User user = userRepository.findByUserGuid(userService.getClaim());

    if (user == null) {
        throw new IllegalStateException("User not found");
    }

    if (!existingSnippet.getUserId().equals(user.getUserId())) {
        throw new IllegalArgumentException("You do not have permission to update this snippet");
    }

    int nextVersionNumber = getNextVersionNumber(snippetId);
    
    Language language = languageRepository.findByLanguageName(snippetDTO.getLanguage());
    if (language == null) {
        throw new IllegalArgumentException("Language not found");
    }

    Version version = new Version(existingSnippet.getSnippetId(), 
                                  nextVersionNumber, 
                                  snippetDTO.getCode());
    versionRepository.save(version);

    return existingSnippet;
}

    public int getNextVersionNumber(Long snippetId) {
        List<Version> versions = versionRepository.findBySnippetId(snippetId);
    
        if (versions != null && !versions.isEmpty()) {
            return versions.size() + 1; 
        } else {
            return 1;
        }
    }

    public void deleteSnippet(@NotNull Long snippetId) {
        snippetRepository.deleteById(snippetId);
    }
}