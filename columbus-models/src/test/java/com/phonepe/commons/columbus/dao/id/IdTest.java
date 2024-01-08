package com.phonepe.commons.columbus.dao.id;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class IdTest {

    @Test
    @DisplayName("auto id")
    void testAutoId() {
        final var id = Ids.auto();
        assertEquals(AutoId.TYPE, id.getType(), "must match type");
    }

    @Test
    @DisplayName("value id")
    void testValueId() {
        final var id = Ids.value("id-value");
        assertAll(
                "value id",
                () -> assertEquals(ValueId.TYPE, id.getType(), "must match type"),
                () -> assertEquals("id-value", id.getValue(), "must contain provided value"));
    }
}
