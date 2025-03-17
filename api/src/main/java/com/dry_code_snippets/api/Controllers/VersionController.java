package com.dry_code_snippets.api.Controllers;

import com.dry_code_snippets.api.Models.Version;
import com.dry_code_snippets.api.Services.VersionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/snippets/versions")
public class VersionController {
    @Autowired
    private VersionService versionService;

    @GetMapping
    public ResponseEntity<List<Version>> getVersionsBySnippetId(@RequestParam("snippetId") Long snippetId) {
        return ResponseEntity.ok(versionService.getVersionsBySnippetId(snippetId));
    }

    @GetMapping("/byVersion")
    public ResponseEntity<Version> getVersionBySnippetIdAndVersionId(@RequestParam("snippetId") Long snippetId, @RequestParam("versionId") Long versionId) {
        return ResponseEntity.ok(versionService.getVersionBySnippetIdAndVersionId(snippetId,versionId));
    }

    @PostMapping
    public ResponseEntity<Version> createVersion(@RequestParam("snippetId") Long snippetId,@RequestBody String code) {
        return ResponseEntity.ok(versionService.createVersion(snippetId,code));
    }

}