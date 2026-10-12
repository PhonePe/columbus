package com.phonepe.columbus.dao.index;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class IndexTest {

    @Test
    @DisplayName("index name")
    void testIndexName() {
        final var index = Indices.name("name");
        assertAll(
                "index name",
                () -> assertEquals(IndexName.TYPE, index.getType(), "must match type"),
                () -> assertEquals("name", index.getName(), "must contain provided name"));
    }

    @Test
    @DisplayName("index reference")
    void testIndexReference() {
        final var index = Indices.reference("key");
        assertAll(
                "index reference",
                () -> assertEquals(IndexReference.TYPE, index.getType(), "must match type"),
                () -> assertEquals("key", index.getKey(), "must contain provided key"));
    }
}
