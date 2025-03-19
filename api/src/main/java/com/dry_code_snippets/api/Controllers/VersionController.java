package com.dry_code_snippets.api.Controllers;

import com.dry_code_snippets.api.Models.Version;
import com.dry_code_snippets.api.Services.AIService;
import com.dry_code_snippets.api.Services.SharedService;
import com.dry_code_snippets.api.Services.VersionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/snippets/versions")
public class VersionController {

    private VersionService versionService;
    private SharedService sharedService;
    
    @Autowired
    public VersionController(VersionService versionService,SharedService sharedService, AIService aiService)
    {
        this.versionService = versionService;
        this.sharedService = sharedService;
    }

    @GetMapping
    public ResponseEntity<List<Version>> getVersionsBySnippetId(@RequestParam("snippetId") Long snippetId) {
        return sharedService.resourceExists(snippetId) ?
         ResponseEntity.ok(versionService.getVersionsBySnippetId(snippetId)):
         ResponseEntity.notFound().build() ;
    }

    @GetMapping("/byVersion")
    public ResponseEntity<Version> getVersionBySnippetIdAndVersionId(@RequestParam("snippetId") Long snippetId, @RequestParam("version") Long version) {
        if(!sharedService.resourceExists(snippetId))
        {
            return ResponseEntity.notFound().build();
        }
        try {
            return ResponseEntity.ok(versionService.getVersionBySnippetIdAndVersionId(snippetId,version));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}