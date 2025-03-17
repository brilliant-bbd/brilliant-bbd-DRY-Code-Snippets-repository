package com.dry_code_snippets.api.Versions;


import com.dry_code_snippets.api.Models.Version;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class VersionTest {
    @Test
    public void testSnippetRecordCreation() {
        Version version = new Version(1L,5,"System.out.println(\"Hello World\");");

        assertNotNull(version);
        assertEquals(1L, version.getSnippetId());
        assertEquals(5, version.getVersionNum());
        assertEquals("System.out.println(\"Hello World\");", version.getCode());
    }
}
