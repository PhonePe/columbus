package com.phonepe.platform.columbus.core.models.operations;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@SuperBuilder
@Data
@EqualsAndHashCode(callSuper = true)
public class EntityGetOperation<T> extends StorageOperation implements RoutingKey {
    @NotEmpty
    @NonNull
    private String documentId;

    @NotNull
    @NonNull
    private Class<T> entityClassType;

    private String routingKey;

    @Override
    protected @NonNull OperationType getOperationType() {
        return OperationType.ENTITY_GET;
    }
}
