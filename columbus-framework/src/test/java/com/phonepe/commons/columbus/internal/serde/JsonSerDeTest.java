package com.phonepe.commons.columbus.internal.serde;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.phonepe.commons.columbus.registry.MutableRegistry;
import com.phonepe.commons.columbus.registry.Registries;
import com.phonepe.commons.columbus.serde.SerDes;
import com.phonepe.commons.columbus.type.JavaType;
import com.phonepe.commons.columbus.type.NamedType;
import com.phonepe.commons.columbus.type.TypeResolver;
import com.phonepe.commons.columbus.type.Types;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

class JsonSerDeTest {

    private static final Map<String, String> SAMPLE_MAP = Map.of("type", "data");
    private static final Sample SAMPLE = Sample.builder().type("data").build();
    private static final String JSON_DATA = "{\"type\":\"data\"}";
    private static final byte[] JSON_BYTES = JSON_DATA.getBytes();
    private static final JavaType<Map<String, String>> mapType = Types.forTypeReference(new TypeReference<>() {});
    private static final JavaType<Sample> sampleType = Types.forClass(Sample.class);
    private static final NamedType<Map<String, String>> mapNamedType = Types.forName("map");
    private static final NamedType<Sample> sampleNamedType = Types.forName("sample");
    private static final NamedType<Sample> missingType = Types.forName("missing");
    private static final MutableRegistry<String, JavaType<?>> typeRegistry = Registries.stored();
    private static final TypeResolver resolver = Types.resolver(Types.typeRegistry(typeRegistry));
    private static final ObjectMapper mapper = new ObjectMapper();
    private static final JsonSerDe serDe = SerDes.json(mapper);

    @Test
    @DisplayName("format")
    void testFormat() {
        assertEquals("json", serDe.format(), "serde uses json format");
    }

    @Test
    @DisplayName("media type")
    void testMediaType() {
        assertEquals("application/json", serDe.mediaType(), "serde has json media type");
    }

    @BeforeAll
    static void beforeAll() {
        typeRegistry.register(mapNamedType.getName(), mapType);
        typeRegistry.register(sampleNamedType.getName(), sampleType);
    }

    @Data
    @Builder
    @Jacksonized
    private static final class Sample {

        private final String type;
    }

    @Nested
    class DeserializationTest {

        private static final JsonSerDe serDe = SerDes.json(new ObjectMapper(), resolver);

        @Test
        @DisplayName("deserialize from string")
        void testFromString() {
            final var data = serDe.fromString(JSON_DATA, mapType);
            assertEquals("data", data.get("type"), "deserialization retains types");
        }

        @Test
        @DisplayName("deserialize from string to named type")
        void testFromStringByName() {
            final var data = serDe.fromString(JSON_DATA, mapNamedType);
            assertEquals("data", data.get("type"), "deserialization retains types");
        }

        @Test
        @DisplayName("deserialize from invalid string")
        void testFromInvalidString() {
            assertThrows(
                    IllegalArgumentException.class,
                    () -> serDe.fromString("{type:data}", mapType),
                    "invalid data should throw");
        }

        @Test
        @DisplayName("deserialize string to invalid type")
        void testStringToInvalidType() {
            assertThrows(
                    IllegalArgumentException.class,
                    () -> serDe.fromString(JSON_DATA, missingType),
                    "invalid type should throw");
        }

        @Test
        @DisplayName("deserialize from bytes")
        void testFromBytes() {
            final var data = serDe.fromBytes(JSON_BYTES, sampleType);
            assertEquals(SAMPLE, data, "deserialization retains types");
        }

        @Test
        @DisplayName("deserialize from bytes to named type")
        void testFromBytesByName() {
            final var data = serDe.fromBytes(JSON_BYTES, sampleNamedType);
            assertEquals(SAMPLE, data, "deserialization retains types");
        }

        @Test
        @DisplayName("deserialize from invalid bytes")
        void testFromInvalidBytes() {
            final var data = "{type:data}".getBytes();

            assertThrows(
                    IllegalArgumentException.class,
                    () -> serDe.fromBytes(data, sampleType),
                    "invalid data should throw");
        }

        @Test
        @DisplayName("deserialize bytes to invalid type")
        void testBytesToInvalidType() {
            assertThrows(
                    IllegalArgumentException.class,
                    () -> serDe.fromBytes(JSON_BYTES, missingType),
                    "invalid type should throw");
        }
    }

    @Nested
    class ConversionTest {

        private static final JsonSerDe serDe = SerDes.json(new ObjectMapper(), resolver);

        @Test
        @DisplayName("convert to java type")
        void testConvert() {
            final var data = serDe.convert(SAMPLE_MAP, sampleType);
            assertEquals(SAMPLE, data, "convert copies fields");
        }

        @Test
        @DisplayName("convert to named type")
        void testConvertByName() {
            final var data = serDe.convert(SAMPLE_MAP, sampleNamedType);
            assertEquals(SAMPLE, data, "convert copies fields");
        }

        @Test
        @DisplayName("convert to invalid type")
        void testConvertToInvalidType() {
            assertThrows(
                    IllegalArgumentException.class,
                    () -> serDe.convert(SAMPLE_MAP, missingType),
                    "invalid type should throw");
        }
    }

    @Nested
    class SerializationTest {

        @Test
        @DisplayName("write serializable data to string")
        void testToString() {
            final var string = serDe.toString(SAMPLE_MAP);
            assertEquals(JSON_DATA, string, "serialization should be in JSON format");
        }

        @Test
        @DisplayName("write serializable data to bytes")
        void testToBytes() {
            final var bytes = serDe.toBytes(SAMPLE_MAP);
            assertArrayEquals(JSON_BYTES, bytes, "serialization should be in JSON format");
        }

        @Test
        @DisplayName("write serializable data to human-readable string")
        void testHumanize() {
            final var string = serDe.humanize(SAMPLE_MAP);
            assertEquals("{\n  \"type\" : \"data\"\n}", string, "serialization should be in JSON format");
        }
    }

    @Nested
    class SerializationFailureTest {

        private static final ObjectMapper mockMapper = mock();

        private static final JsonSerDe serDe = SerDes.json(mockMapper);

        @Test
        @DisplayName("write non-serializable data to string")
        void testToStringFail() throws JsonProcessingException {
            doThrow(JsonProcessingException.class)
                    .when(mockMapper)
                    .writeValueAsString(any());

            assertThrows(
                    IllegalArgumentException.class,
                    () -> serDe.toString(SAMPLE_MAP),
                    "serialization should fail for non-serializable data");
        }

        @Test
        @DisplayName("write non-serializable data to bytes")
        void testToBytesFail() throws JsonProcessingException {
            doThrow(JsonProcessingException.class)
                    .when(mockMapper)
                    .writeValueAsBytes(any());

            assertThrows(
                    IllegalArgumentException.class,
                    () -> serDe.toBytes(SAMPLE_MAP),
                    "serialization should fail for non-serializable data");
        }

        @Test
        @DisplayName("write non-serializable data to human readable string")
        void testHumanizeFail() throws JsonProcessingException {
            final ObjectWriter mockWriter = mock();
            doReturn(mockWriter)
                    .when(mockMapper)
                    .writerWithDefaultPrettyPrinter();
            doThrow(JsonProcessingException.class)
                    .when(mockWriter)
                    .writeValueAsString(any());

            assertThrows(
                    IllegalArgumentException.class,
                    () -> serDe.humanize(SAMPLE_MAP),
                    "serialization should fail for non-serializable data");
        }
    }
}
