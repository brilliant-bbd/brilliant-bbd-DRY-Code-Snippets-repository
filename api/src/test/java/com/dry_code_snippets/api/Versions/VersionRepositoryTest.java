package com.dry_code_snippets.api.Versions;

import com.dry_code_snippets.api.Models.Version;
import com.dry_code_snippets.api.Repositories.VersionRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import static org.assertj.core.api.Assertions.assertThat;
import java.util.List;

@DataJpaTest
public class VersionRepositoryTest {

    @Autowired
    private VersionRepository versionRepository;

    @Test
    public void testFindBySnippetId() {
        Version version1 = new Version(1L, 1, "Code snippet v1");
        Version version2 = new Version(1L, 2, "Code snippet v2");
        versionRepository.save(version1);
        versionRepository.save(version2);

        List<Version> versions = versionRepository.findBySnippetId(1L);

        assertThat(versions).hasSize(2);
        assertThat(versions.get(0).getVersionNum()).isEqualTo(1);
        assertThat(versions.get(1).getVersionNum()).isEqualTo(2);
    }
}
