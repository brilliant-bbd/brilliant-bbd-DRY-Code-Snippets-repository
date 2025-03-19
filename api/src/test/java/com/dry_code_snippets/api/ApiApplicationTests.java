package com.dry_code_snippets.api;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = Main.class)
class MainApplicationTests {

    @Test
    void contextLoads() {
        assertThat(true).isTrue();
    }
}
