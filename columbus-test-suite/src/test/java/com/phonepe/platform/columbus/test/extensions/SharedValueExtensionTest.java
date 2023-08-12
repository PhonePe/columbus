package com.phonepe.platform.columbus.test.extensions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.phonepe.platform.columbus.internal.serde.JsonSerDe;
import com.phonepe.platform.columbus.serde.SerDe;
import com.phonepe.platform.columbus.test.extensions.mapper.WithMapper;
import com.phonepe.platform.columbus.test.extensions.serde.WithSerDe;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;

@WithMapper.Root
class SharedValueExtensionTest {

    @Nested
    class MapperParameterTest {

        @Test
        @DisplayName("provides mapper")
        void testMapperIsPresent(final ObjectMapper mapper) {
            assertNotNull(mapper, "must provide a valid mapper");
        }
    }

    @Nested
    @WithSerDe.Root
    class SerDeParameterTest {

        @Test
        @DisplayName("provides serde")
        void testSerDeIsPresent(final SerDe serDe, final ObjectMapper mapper) {
            assertInstanceOf(JsonSerDe.class, serDe, "must provide a valid json serde");
            assertSame(mapper, ((JsonSerDe) serDe).getMapper(), "must use the mapper provided");
        }
    }
}
