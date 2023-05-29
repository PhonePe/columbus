package com.phonepe.platform.columbus.core.models.operations;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.phonepe.platform.columbus.core.models.EntityType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "operationType")
@JsonSubTypes({
    @JsonSubTypes.Type(value = CreateIndexOperation.class, name = "CREATE_INDEX"),
    @JsonSubTypes.Type(value = CreateMappingOperation.class, name = "CREATE_MAPPING"),
    @JsonSubTypes.Type(value = UpdateMappingOperation.class, name = "UPDATE_MAPPING"),
    @JsonSubTypes.Type(value = CreateTemplateOperation.class, name = "CREATE_TEMPLATE"),
    @JsonSubTypes.Type(value = EntitySaveOperation.class, name = "ENTITY_SAVE"),
    @JsonSubTypes.Type(value = EntityUpdateOperation.class, name = "ENTITY_UPDATE"),
    @JsonSubTypes.Type(value = EntityFieldsUpdateOperation.class, name = "ENTITY_UPDATE_FIELDS"),
    @JsonSubTypes.Type(value = EntityDeleteOperation.class, name = "ENTITY_DELETE"),
    @JsonSubTypes.Type(value = EntityGetOperation.class, name = "ENTITY_GET"),
    @JsonSubTypes.Type(value = MultiGetOperation.class, name = "ENTITY_MULTI_GET"),
        @JsonSubTypes.Type(value = EntitiesSearchOperation.class, name = "ENTITIES_SEARCH"),
        @JsonSubTypes.Type(value = UnitSearchOperation.class, name = "UNIT_SEARCH"),
    @JsonSubTypes.Type(value = EntitiesDeleteOperation.class, name = "ENTITY_MULTI_DELETE"),
})
@SuperBuilder
@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class StorageOperation {
    @NonNull
    private EntityType entityType;
    @NonNull
    private String indexName;

    @NonNull
    protected abstract OperationType getOperationType();
}
