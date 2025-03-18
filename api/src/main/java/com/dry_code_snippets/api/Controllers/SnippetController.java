package com.dry_code_snippets.api.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.dry_code_snippets.DTO.SnippetDTO;
import com.dry_code_snippets.api.Models.Snippet;
import com.dry_code_snippets.api.Services.SnippetService;

import jakarta.validation.Valid;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/snippets")
public class SnippetController {
    private final SnippetService snippetService;

    @Autowired
    public SnippetController(SnippetService snippetService) {
        this.snippetService = snippetService;
    }

    // Get all snippets
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

    // Get a snippet by ID
    @GetMapping("/{snippetId}")
    public ResponseEntity<SnippetDTO> getSnippetById(@PathVariable("snippetId") Long snippetId) {
        Optional<SnippetDTO> snippet = Optional.of(snippetService.getSnippetById(snippetId));
        return snippet.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Create a new snippet
    @PostMapping
    public ResponseEntity<Snippet> createSnippet(@RequestBody @Valid SnippetDTO snippet) {
        Snippet createdSnippet = snippetService.createSnippet(snippet);
        return new ResponseEntity<>(createdSnippet, HttpStatus.CREATED);
    }

    // Update a snippet
    @PutMapping("/{id}")
    public ResponseEntity<Snippet> updateSnippet(@PathVariable("id") Long id, @RequestBody String snippet) {
        Snippet updatedSnippet = snippetService.updateSnippet(id, snippet);
        return updatedSnippet != null ? ResponseEntity.ok(updatedSnippet) : ResponseEntity.badRequest().build();
    }

    // Delete a snippet
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSnippet(@PathVariable("id") Long id) {
        snippetService.deleteSnippet(id);
        return ResponseEntity.noContent().build();
    }
}