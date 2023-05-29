package com.phonepe.platform.columbus.core.models.operations;

import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotEmpty;

@SuperBuilder
@Data
@EqualsAndHashCode(callSuper = true)
public class EntitiesDeleteOperation extends StorageOperation implements RoutingKey {
    @NotEmpty
    @NonNull
    private List<String> documentId;

    private String routingKey;

    @Override
    protected @NonNull OperationType getOperationType() {
        return OperationType.ENTITY_MULTI_DELETE;
    }
}
