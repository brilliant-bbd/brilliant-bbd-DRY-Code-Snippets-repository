package com.dry_code_snippets.api.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
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

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class SnippetService {

    private final SnippetRepository snippetRepository;
    private final UserRepository userRepository;
    private final VersionRepository versionRepository;
    private final LanguageRepository languageRepository;

    @Autowired
    public SnippetService(SnippetRepository snippetRepository, UserRepository userRepository,
            VersionRepository versionRepository, LanguageRepository languageRepository) {
        this.snippetRepository = snippetRepository;
        this.userRepository = userRepository;
        this.versionRepository = versionRepository;
        this.languageRepository = languageRepository;
    }

    private UUID getClaim() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UUID userGuid = null;
        if (authentication instanceof JwtAuthenticationToken) {
            JwtAuthenticationToken jwtToken = (JwtAuthenticationToken) authentication;
            Jwt jwt = jwtToken.getToken();

            userGuid = convertStringToUUID(jwt.getClaim("sub"));
        }
        return userGuid;
    }

    public List<Snippet> getAllSnippets(String tag, String language) {
        if (tag != null && !tag.isEmpty() && language != null && !language.isEmpty()) {
            List<String> tags = Arrays.asList(tag.split(";"));
            return snippetRepository.findByLanguageAndTagsAndNotDeleted(language, tags);
        }

        if (language != null && !language.isEmpty()) {
            return snippetRepository.findByLanguageAndNotDeleted(language);
        }

        if (tag != null && !tag.isEmpty()) {
            List<String> tags = Arrays.asList(tag.split(";"));
            return snippetRepository.findByTagsAndNotDeleted(tags);
        }

        return snippetRepository.findAllByDeletedFalse();
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

        User user = userRepository.findByUserGuid(getClaim());
        if (user == null) {
            user = new User(getClaim());
            user = userRepository.save(user);
        }

        Snippet snippet = new Snippet(user.getUserId(), snippetDTO.getTitle(), snippetDTO.getDescription(),
                language.getLanguageId());
        Snippet savedSnippet = snippetRepository.save(snippet);

        Version version = new Version(savedSnippet.getSnippetId(), "version 1", snippetDTO.getCode());
        versionRepository.save(version);

        return savedSnippet;
    }

    public static UUID convertStringToUUID(String largeNumberString) {
        if (largeNumberString == null || largeNumberString.length() > 32) {
            throw new IllegalArgumentException("String is too long or invalid for a valid UUID.");
        }

        while (largeNumberString.length() < 32) {
            largeNumberString = "0" + largeNumberString;
        }

        String highBitsString = largeNumberString.substring(0, 16);
        String lowBitsString = largeNumberString.substring(16);

        long highBits = Long.parseLong(highBitsString, 16);
        long lowBits = Long.parseLong(lowBitsString, 16);

        return new UUID(highBits, lowBits);
    }

    @Transactional
public Snippet updateSnippet(Long snippetId, SnippetDTO snippetDTO) {
    Snippet existingSnippet = snippetRepository.findById(snippetId).orElse(null);
    
    if (existingSnippet == null) {
        throw new IllegalArgumentException("Snippet not found");
    }

    User user = userRepository.findByUserGuid(getClaim());

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
                                  "version " + nextVersionNumber, 
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

    public void deleteSnippet(Long snippetId) {
        snippetRepository.deleteById(snippetId);
    }
}