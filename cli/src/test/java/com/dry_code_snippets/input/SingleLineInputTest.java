package com.dry_code_snippets.input;

import com.dry_code_snippets.util.InputHelper;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

class SingleLineInputTest {

    private String simulateUserInput(String input, int maxLength, boolean allowEmpty, boolean integerOnly) {
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        return InputHelper.singleLineInput("Enter value:", maxLength, allowEmpty, integerOnly);
    }

    @Test
    void testValid() {
        assertEquals("hello", simulateUserInput("hello\n", 10, false, false));
    }

    @Test
    void testMaxLength() {
        assertEquals("valid", simulateUserInput("exceedslimit\nvalid\n", 11, false, false));
    }

    @Test
    void testEmptyAllowed() {
        assertEquals("", simulateUserInput("\n", 10, true, false));
    }

    @Test
    void testEmptyAllowed2() {
        assertEquals("valid", simulateUserInput("valid\n", 10, true, false));
    }

    @Test
    void testEmptyNotAllowed() {
        assertEquals("valid", simulateUserInput("\nvalid\n", 10, false, false));
    }

    @Test
    void testIntegerOnlyValid() {
        assertEquals("123", simulateUserInput("123\n", 10, false, true));
    }

    @Test
    void testIntegerOnlyInvalid() {
        assertEquals("456", simulateUserInput("abc\n456\n", 10, false, true));
    }
}
