package com.dry_code_snippets.input;

import com.dry_code_snippets.util.InputHelper;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

class MultiLineInputTest {

    private String simulateUserMultiLineInput(String input, int maxLength, boolean allowEmpty) {
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        return InputHelper.multiLineInput("Enter multiple lines:", maxLength, allowEmpty);
    }

    @Test
    void testValid() {
        String input = "This is line 1\nThis is line 2\n$DONE\n";
        assertEquals("This is line 1\nThis is line 2", simulateUserMultiLineInput(input, 100, false));
    }
    @Test
    void testMaxLengthExceeded() {
        String input = "This is a long input that exceeds the limit.\n$DONE\nValid input\n$DONE\n";
        assertEquals("Valid input", simulateUserMultiLineInput(input, 20, false));
    }

    @Test
    void testEmptyAllowed() {
        assertEquals("", simulateUserMultiLineInput("$DONE\n", 100, true));
    }

    @Test
    void testEmptyNotAllowed() {
        String input = "\n$DONE\nValid Input\n$DONE\n";
        assertEquals("Valid Input", simulateUserMultiLineInput(input, 100, false));
    }

    @Test
    void testWithSpacesOnly() {
        String input = "     \n$DONE\nValid\n$DONE\n";
        assertEquals("Valid", simulateUserMultiLineInput(input, 100, false));
    }
}
