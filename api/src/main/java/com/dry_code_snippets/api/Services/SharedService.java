package com.dry_code_snippets.api.Services;

import com.dry_code_snippets.DTO.SnippetDTO;
import com.dry_code_snippets.api.Models.Snippet;
import com.dry_code_snippets.api.Models.User;

import com.dry_code_snippets.api.Repositories.SnippetRepository;
import com.dry_code_snippets.api.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class SharedService {
    private final UserRepository userRepository;
    private final UserService userService;
    private final SnippetRepository snippetRepository;

    @Autowired
    public SharedService(UserRepository userRepository, UserService userService, SnippetRepository snippetRepository) {
        this.userRepository = userRepository;
        this.userService = userService;
        this.snippetRepository = snippetRepository;
    }

    public boolean ownsResource(Long snippetId) {
        Optional<User> user = userRepository.findByUserGuid(userService.getClaim());
        Optional<Snippet> snippet = snippetRepository.findById(snippetId);

        if (user.get().getUserId().equals(snippet.get().getUserId())) {
            return true;
        }
        return false;
    }

    public boolean resourceExists(Long snippetId)
    {
        Optional<SnippetDTO> snippet = snippetRepository.findSnippetDtoById(snippetId);

        if (snippet.isPresent()){
            return true;
        }
        return false;
    }
}
