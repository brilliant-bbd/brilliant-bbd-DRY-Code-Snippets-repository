package com.dry_code_snippets.api.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.dry_code_snippets.api.DTO.SnippetDTO;
import com.dry_code_snippets.api.Models.Snippet;
import com.dry_code_snippets.api.Services.AIService;
import com.dry_code_snippets.api.Services.SharedService;
import com.dry_code_snippets.api.Services.SnippetService;

import jakarta.validation.Valid;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/snippets")
public class SnippetController {
    private final SnippetService snippetService;
    private final SharedService sharedService;
    private final AIService aiService;

    @Autowired
    public SnippetController(SnippetService snippetService, SharedService sharedService, AIService aiService) {
        this.snippetService = snippetService;
        this.sharedService = sharedService;
        this.aiService = aiService;
    }

    @GetMapping
    public ResponseEntity<List<SnippetDTO>> getAllSnippets(
            @RequestParam(required = false) String tags,
            @RequestParam(required = false) String language) {

        List<SnippetDTO> snippets = snippetService.getAllSnippets(Optional.of(tags), Optional.of(language));

        if (snippets.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(snippets);
    }

    @GetMapping("/{snippetId}")
    public ResponseEntity<SnippetDTO> getSnippetById(@PathVariable("snippetId") Long snippetId) {
        Optional<SnippetDTO> snippet = snippetService.getSnippetById(snippetId);
        return snippet.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Snippet> createSnippet(@RequestBody @Valid SnippetDTO snippet) {
        Snippet createdSnippet = snippetService.createSnippet(snippet);
        return new ResponseEntity<>(createdSnippet, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Snippet> updateSnippet(@PathVariable("id") Long id, @RequestBody String snippet) {
        boolean resourceExists = sharedService.resourceExists(id);
        if (!resourceExists) {
            return ResponseEntity.notFound().build();
        }
        boolean canUpdate = sharedService.ownsResource(id);
        if (!canUpdate) {
            return ResponseEntity.status(403).build();
        }

        Snippet updatedSnippet = snippetService.updateSnippet(id, snippet);
        return updatedSnippet != null ? ResponseEntity.ok(updatedSnippet) : ResponseEntity.badRequest().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSnippet(@PathVariable("id") Long id) {
        boolean resourceExists = sharedService.resourceExists(id);
        if (!resourceExists) {
            return ResponseEntity.notFound().build();
        }
        boolean canDelete = sharedService.ownsResource(id);
        if (!canDelete) {
            return ResponseEntity.status(403).build();
        }
        Long deletedId = snippetService.deleteSnippet(id);
        if (deletedId != null) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}/explain")
    public Mono<String> explainSnippet(@PathVariable("id") Long id) {
        return aiService.explainSnippet(id);
    }
}