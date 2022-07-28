package com.phonepe.platform.columbus.core.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Preconditions;
import com.phonepe.platform.columbus.core.exception.ColumbusException;
import com.phonepe.platform.columbus.models.core.exceptions.ColumbusErrorCode;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;


@Slf4j
@UtilityClass
public class MapperUtils {
    private ObjectMapper mapper;

    public void initialise(final ObjectMapper objectMapper) {
        mapper = objectMapper;
    }

    public ObjectMapper getObjectMapper() {
        Preconditions.checkNotNull(mapper, "Please call MapperUtils.init(objectMapper) to set mapper");
        return mapper;
    }

    public <T> String writeValueAsString(T value) {
        return writeValueAsString(getObjectMapper(), value);
    }

    public <T> String writeValueAsString(ObjectMapper mapper, T value) {
        try {
            return mapper.writeValueAsString(value);
        } catch (Exception e) {
            throw error(ColumbusErrorCode.SERIALIZATION_ERROR, "Failed to write value as string", e);
        }
    }

    public <T> T readValue(InputStream source, Class<T> type) {
        return readValue(getObjectMapper(), source, type);
    }

    public <T> T readValue(ObjectMapper mapper, InputStream input, Class<T> type) {
        try {
            return mapper.readValue(input, type);
        } catch (Exception e) {
            throw deserializationError(e);
        }
    }

    public <T> T readValue(InputStream source, TypeReference<T> type) {
        return readValue(getObjectMapper(), source, type);
    }

    public <T> T readValue(ObjectMapper mapper, InputStream source, TypeReference<T> type) {
        try {
            return mapper.readValue(source, type);
        } catch (Exception e) {
            throw deserializationError(e);
        }
    }

    public <T> T copyValue(T source) {
        return copyValue(getObjectMapper(), source);
    }

    public <T> T copyValue(ObjectMapper mapper, T source) {
        try {
            //noinspection unchecked
            return (T) mapper.convertValue(source, source.getClass());
        } catch (Exception e) {
            throw error(ColumbusErrorCode.DESERIALIZATION_ERROR, "Failed to copy value", e);
        }
    }

    private ColumbusException deserializationError(Exception e) {
        return error(ColumbusErrorCode.DESERIALIZATION_ERROR, "Failed to read value from stream", e);
    }

    private ColumbusException error(ColumbusErrorCode code, String message, Exception e) {
        log.error(message, e);
        return ColumbusException.propagate(code, message, e);
    }
}