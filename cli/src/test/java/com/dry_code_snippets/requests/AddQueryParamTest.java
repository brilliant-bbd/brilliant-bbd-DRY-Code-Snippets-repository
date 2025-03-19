package com.dry_code_snippets.requests;

import static org.junit.jupiter.api.Assertions.*;

import com.dry_code_snippets.util.RequestHandler;
import org.junit.jupiter.api.Test;

class AddQueryParamTest {

    @Test
    void testAddQueryParamToNullQuery() {
        String result = RequestHandler.addQueryParam(null, "key", "value");
        assertEquals("key=value", result);
    }

    @Test
    void testAddQueryParamToExisting() {
        String result = RequestHandler.addQueryParam("key1=value1", "key2", "value2");
        assertEquals("key1=value1&key2=value2", result);
    }

    @Test
    void testAddQueryToEmptyQuery() {
        String result = RequestHandler.addQueryParam("   ", "key", "value");
        assertEquals("key=value", result);
    }

    @Test
    void testAddQueryWithEmptyValue() {
        String result = RequestHandler.addQueryParam("key1=value1", "key2", "");
        assertEquals("key1=value1&key2=", result);
    }
}
