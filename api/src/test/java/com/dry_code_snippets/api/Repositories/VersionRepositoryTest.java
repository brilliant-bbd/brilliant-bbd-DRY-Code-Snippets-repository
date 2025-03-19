package com.dry_code_snippets.api.Repositories;

import com.dry_code_snippets.api.Models.Version;
import com.dry_code_snippets.api.Repositories.VersionRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import static org.assertj.core.api.Assertions.assertThat;
import java.util.List;
import java.util.Optional;

@DataJpaTest
public class VersionRepositoryTest {

    @Autowired
    private VersionRepository versionRepository;

    @Test
    public void testFindBySnippetId() {
        Version version1 = new Version(1L, 1L, "Code snippet v1");
        Version version2 = new Version(1L, 2L, "Code snippet v2");
        versionRepository.save(version1);
        versionRepository.save(version2);

        Optional<List<Version>> versions = versionRepository.findVersionsBySnippetId(1L);

        assertThat(versions.get()).hasSize(2);
        assertThat(versions.get().get(0).getVersion()).isEqualTo(1L);
        assertThat(versions.get().get(1).getVersion()).isEqualTo(2L);
    }
}
