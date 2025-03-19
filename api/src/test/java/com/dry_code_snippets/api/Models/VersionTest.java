package com.dry_code_snippets.api.Models;


import com.dry_code_snippets.api.Models.Version;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class VersionTest {
    @Test
    public void testSnippetRecordCreation() {
        Version version = new Version(1L,5L,"System.out.println(\"Hello World\");");

        assertNotNull(version);
        assertEquals(1L, version.getSnippetId());
        assertEquals(5L, version.getVersion());
        assertEquals("System.out.println(\"Hello World\");", version.getCode());
    }
}
