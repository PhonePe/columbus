package com.phonepe.platform.columbus.core.models.operations;


import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Map;

@SuperBuilder
@Data
@EqualsAndHashCode(callSuper = true)
public class EntityFieldsUpdateOperation extends StorageOperation implements RoutingKey {
    @NonNull
    @NotEmpty
    private String documentId;

    @NotNull
    @NonNull
    private Map<String, Object> entityFieldValueData;

    private int retryCount;

    private String routingKey;

    @Override
    protected @NonNull OperationType getOperationType() {
        return OperationType.ENTITY_UPDATE_FIELDS;
    }
}
