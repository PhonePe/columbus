package com.phonepe.platform.columbus.core.models.operations;


import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotEmpty;

@SuperBuilder
@Data
@EqualsAndHashCode(callSuper = true)
public class EntityUpdateOperation<T> extends StorageOperation implements RoutingKey {
    @NotEmpty
    @NonNull
    private String documentId;

    @NotEmpty
    @NonNull
    private T value;

    // if specified - need to be managed by client who specifies the same if not ES does the work.
    private String routingKey;

    @Override
    protected @NonNull OperationType getOperationType() {
        return OperationType.ENTITY_UPDATE;
    }
}

