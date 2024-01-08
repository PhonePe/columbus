package com.phonepe.commons.columbus.test.extensions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.phonepe.commons.columbus.internal.serde.JsonSerDe;
import com.phonepe.commons.columbus.serde.SerDe;
import com.phonepe.commons.columbus.test.annotations.WithMapper;
import com.phonepe.commons.columbus.test.annotations.WithSerDe;
import com.phonepe.commons.columbus.test.value.ValueProps;
import com.phonepe.commons.columbus.test.value.ValueProps.Key;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;

@WithMapper
class ValueExtensionTest {

    @Nested
    class MapperParameterTest {

        @Test
        @DisplayName("provides mapper")
        void testMapperIsPresent(final ObjectMapper mapper) {
            assertNotNull(mapper, "must provide a valid mapper");
        }
    }

    @Nested
    @WithSerDe
    class SerDeParameterTest {

        @Test
        @DisplayName("provides serde")
        void testSerDeIsPresent(final SerDe serDe, final ObjectMapper mapper) {
            assertInstanceOf(JsonSerDe.class, serDe, "must provide a valid json serde");
            assertSame(mapper, ((JsonSerDe) serDe).getMapper(), "must use the mapper provided");
        }
    }

    @Nested
    @WithMapper(@ValueProps(key = @Key(keyName = "mapper")))
    @WithSerDe(value = @ValueProps(key = @Key(keyClass = JsonSerDe.class)), mapper = @Key(keyName = "mapper"))
    class CustomKeyTest {

        @Test
        @DisplayName("provides serde")
        void testSerDeIsPresent(
                @Key(keyClass = JsonSerDe.class) final SerDe serDe,
                @Key(keyName = "mapper") final ObjectMapper mapper
        ) {
            assertInstanceOf(JsonSerDe.class, serDe, "must provide a valid json serde");
            assertSame(mapper, ((JsonSerDe) serDe).getMapper(), "must use the mapper provided");
        }
    }
}
