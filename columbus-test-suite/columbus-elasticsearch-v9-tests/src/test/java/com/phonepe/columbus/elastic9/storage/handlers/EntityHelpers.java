package com.phonepe.columbus.elastic9.storage.handlers;

import com.phonepe.columbus.dao.id.Id;
import com.phonepe.columbus.dao.id.Ids;
import com.phonepe.columbus.storage.models.entity.Entity;
import com.phonepe.columbus.type.EntityType;
import com.phonepe.columbus.type.Types;
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

    static ElasticEntity document(final String id, final String name, final int count, final Object key) {
        return ElasticEntity.builder()
                .id(id)
                .name(name)
                .count(count)
                .attribute(key)
                .build();
    }

    static Entity<ElasticEntity> entity(final ElasticEntity entity) {
        return Entity.<ElasticEntity>builder()
                .id(Ids.value(entity.getId()))
                .value(entity)
                .build();
    }

    static ElasticEntityId entityId(final ElasticEntity entity) {
        return ElasticEntityId.builder()
                .id(entity.getId())
                .build();
    }

    static Id id(final ElasticEntity entity) {
        return Ids.value(entity.getId());
    }
}
