package com.phonepe.commons.columbus.internal.serde;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.phonepe.commons.columbus.registry.Registries;
import com.phonepe.commons.columbus.serde.SerDe;
import com.phonepe.commons.columbus.type.EntityType;
import com.phonepe.commons.columbus.type.TypeResolver;
import com.phonepe.commons.columbus.type.Types;
import lombok.Builder;
import lombok.Getter;

import java.io.IOException;

/**
 * A serde that provides the serialization and deserialization capabilities for JSON data.
 */
@Builder(toBuilder = true)
public class JsonSerDe implements SerDe {

    @Getter
    private final ObjectMapper mapper;

    @Builder.Default
    private final TypeResolver typeResolver = Types.resolver(Types.typeRegistry(Registries.empty()));

    @Override
    public String format() {
        return "json";
    }

    @Override
    public <T> T fromString(final String data, final EntityType<T> type) {
        if (data == null) {
            return null;
        }
        try {
            return mapper.readValue(data, getTypeReference(type));
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Failed to read string data as '%s'".formatted(type.name()), e);
        }
    }

    @Override
    public <T> T fromBytes(final byte[] data, final EntityType<T> type) {
        if (data == null) {
            return null;
        }
        try {
            return mapper.readValue(data, getTypeReference(type));
        } catch (IOException e) {
            throw new IllegalArgumentException("Failed to read byte data as '%s'".formatted(type.name()), e);
        }
    }

    @Override
    public <T> T convert(final Object data, final EntityType<T> type) {
        return mapper.convertValue(data, getTypeReference(type));
    }

    @Override
    public String toString(final Object data) {
        try {
            return mapper.writeValueAsString(data);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Failed to write data to string", e);
        }
    }

    @Override
    public byte[] toBytes(final Object data) {
        try {
            return mapper.writeValueAsBytes(data);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Failed to write data to bytes", e);
        }
    }

    @Override
    public String humanize(final Object data) {
        try {
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(data);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Failed to pretty write data", e);
        }
    }

    @Override
    public String mediaType() {
        return "application/json";
    }

    private <T> TypeReference<T> getTypeReference(final EntityType<T> type) {
        return typeResolver.toJavaType(type).getType();
    }
}
