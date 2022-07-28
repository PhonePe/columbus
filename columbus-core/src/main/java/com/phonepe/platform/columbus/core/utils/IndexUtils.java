package com.phonepe.platform.columbus.core.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.phonepe.platform.columbus.core.models.EntityType;
import lombok.experimental.UtilityClass;

import java.io.InputStream;

@UtilityClass
public class IndexUtils {

    public String getIndexName(final EntityType entityType, final String name) {
        return String.join(".", "columbus", entityType.name(), name, "index").toLowerCase();
    }

    public String getTemplateName(String indexName) {
        return String.join(".", indexName, "template").toLowerCase();
    }

    public String getIndexPattern(final String indexName) {
        return indexName.toLowerCase();
    }

    public <T> T getSource(String path, TypeReference<T> type) {
        InputStream inputStream = IndexUtils.class.getClassLoader().getResourceAsStream(path);
        return MapperUtils.readValue(inputStream, type);
    }

    public String getCacheName(final EntityType entityType, final String name) {
        return String.join(".", "columbus", "cache", entityType.name(), name).toLowerCase();
    }
}
