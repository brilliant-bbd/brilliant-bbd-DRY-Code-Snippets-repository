package com.dry_code_snippets.api.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

import com.dry_code_snippets.api.Models.Snippet;
import com.dry_code_snippets.api.Models.User;
import com.dry_code_snippets.api.Repositories.SnippetRepository;
import com.dry_code_snippets.api.Repositories.UserRepository;

import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class SnippetService {

    private final SnippetRepository snippetRepository;
    private final UserRepository userRepository;
    private UUID userGuid = null;

    @Autowired
    public SnippetService(SnippetRepository snippetRepository, UserRepository userRepository) {
        this.snippetRepository = snippetRepository;
        this.userRepository = userRepository;

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

    public List<Snippet> getAllSnippets() {
        return snippetRepository.findAll();
    }

    public Optional<Snippet> getSnippetById(Long snippetId) {
        return snippetRepository.findById(snippetId);
    }

    @Transactional
    public Snippet createSnippet(Snippet snippet) {
        System.out.println(getClaim());
        User user = userRepository.findByUserGuid(getClaim());

        if (user == null) {
            user = new User(userGuid);
            userRepository.save(user);
        }

        snippet.setUserId(user.getUserGuid());

        return snippetRepository.save(snippet);
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

    public Snippet updateSnippet(Long snippetId, Snippet snippet) {
        // if (snippetRepository.existsById(snippetId)) {
        // Snippet updatedSnippet = new Snippet(
        // snippetId,
        // snippet.getTitle(),
        // snippet.getDescription(),
        // snippet.getCode(),
        // snippet.getLanguageId(),
        // snippet.getCreatedAt(),
        // snippet.getIsDeleted(),
        // snippet.getCreatedAt()
        // );
        // return snippetRepository.save(updatedSnippet);
        // }
        return null;
    }

    public void deleteSnippet(Long snippetId) {
        snippetRepository.deleteById(snippetId);
    }
}