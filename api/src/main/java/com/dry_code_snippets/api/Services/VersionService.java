package com.dry_code_snippets.api.Services;

import com.dry_code_snippets.api.Models.Version;
import com.dry_code_snippets.api.Repositories.VersionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class VersionService {
    @Autowired
    private VersionRepository versionRepository;

    public List<Version> getVersionsBySnippetId(Long snippetId) {
        return versionRepository.findBySnippetId(snippetId).orElse(Collections.emptyList());
    }

    public Version createVersion(Long snippetId, String code) {
        Version version =  versionRepository.save(new Version(snippetId, 1L, code));

        Long versionNum= (version.getVersionId()+1L);
        return versionRepository.save(new Version(snippetId,versionNum,code));
    }

    public Version getVersionBySnippetIdAndVersionId( Long snippetId, Long versionId){
        List<Version> versions = getVersionsBySnippetId(snippetId);

        if (versions.isEmpty()) {
            throw new NoSuchElementException("No versions found for snippet ID: " + snippetId);
        }

        List<Version> filteredVersions = versions.stream()
                .filter(version -> version.getVersionId().equals(versionId))
                .toList();

        if (filteredVersions.isEmpty()) {
            throw new NoSuchElementException("No version found with ID: " + versionId + " for snippet ID: " + snippetId);
        }
        return versions.getFirst();
    }
}