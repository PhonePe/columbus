package com.phonepe.commons.columbus.test.extensions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.phonepe.commons.columbus.internal.serde.JsonSerDe;
import com.phonepe.commons.columbus.serde.SerDe;
import com.phonepe.commons.columbus.test.annotations.WithSerDe;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertSame;

@WithSerDe.All
class AllValuesExtensionTest {

    @Test
    @DisplayName("provides mapper and serde")
    void testSerDeIsPresent(final SerDe serDe, final ObjectMapper mapper) {
        assertInstanceOf(JsonSerDe.class, serDe, "must provide a valid json serde");
        assertSame(mapper, ((JsonSerDe) serDe).getMapper(), "must use the mapper provided");
    }
}
