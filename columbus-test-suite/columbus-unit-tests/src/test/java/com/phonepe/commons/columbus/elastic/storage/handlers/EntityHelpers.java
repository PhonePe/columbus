package com.phonepe.commons.columbus.elastic.storage.handlers;

import com.phonepe.commons.columbus.type.EntityType;
import com.phonepe.commons.columbus.type.Types;
import lombok.experimental.UtilityClass;

import java.util.HashMap;
import java.util.Map;

@UtilityClass
class EntityHelpers {

    final EntityType<ElasticEntity> ENTITY_TYPE = Types.forClass(ElasticEntity.class);
    final EntityType<ElasticEntityId> ENTITY_ID_TYPE = Types.forClass(ElasticEntityId.class);
    final Map<String, Object> BASE_PROPERTIES = Map.of(
            "id", Map.of("type", "keyword"),
            "name", Map.of("type", "keyword"),
            "count", Map.of("type", "integer"));
    final Map<String, Object> SCHEMA = Map.of("properties", BASE_PROPERTIES);

    Map<String, Object> schemaWithAttribute(final Map<String, Object> attribute) {
        final var properties = new HashMap<>(BASE_PROPERTIES);
        properties.put("attribute", attribute);
        return Map.of("properties", properties);
    }

    Map<String, Object> schemaWithAttributeType(final String type) {
        return schemaWithAttribute(Map.of("type", type));
    }
}
