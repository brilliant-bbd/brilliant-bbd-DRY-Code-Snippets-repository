package com.dry_code_snippets.api.Services;

import com.dry_code_snippets.api.Models.Version;
import com.dry_code_snippets.api.Repositories.SnippetRepository;
import com.dry_code_snippets.api.Repositories.VersionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class VersionService {
    @Autowired
    private VersionRepository versionRepository;
    private SnippetRepository snippetRepository;

    public List<Version> getVersionsBySnippetId(Long snippetId) {
        return versionRepository.findBySnippetId(snippetId).orElse(Collections.emptyList());
    }

    public Version createVersion(Long snippetId, String code) {
        if (!snippetRepository.existsById(snippetId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Snippet not found");
        }
            Long versionNum=getNewVersionNum(snippetId)+1;
            Version version=new Version(snippetId,versionNum,code);
            return versionRepository.save(version);
    }

    public Long getNewVersionNum(Long snippetId){
        var versionList = getVersionsBySnippetId(snippetId);
        return versionList.stream()
                .mapToLong(Version::getVersion)
                .max()
                .orElse(0);
    }

    public Version getVersionBySnippetIdAndVersionId(Long snippetId, Long version) {
        List<Version> versions = getVersionsBySnippetId(snippetId);

        if (versions.isEmpty()) {
            throw new NoSuchElementException("No versions found for snippet ID: " + snippetId);
        }

        List<Version> filteredVersions = versions.stream()
                .filter(v -> v.getVersion().equals(version))
                .toList();
        
        if (filteredVersions.isEmpty()) {
            throw new NoSuchElementException("No version found with ID: " + version + " for snippet ID: " + snippetId);
        }
        return filteredVersions.getFirst();
    }
}