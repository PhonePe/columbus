package com.phonepe.platform.columbus.core.models.operations;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@SuperBuilder
@Data
@EqualsAndHashCode(callSuper = true)
public class MultiGetOperation<T> extends StorageOperation {
    @Size(min = 1)
    private List<String> documentIds;

    @NotNull
    @NonNull
    private Class<T> entityClassType;

    @Override
    protected @NonNull OperationType getOperationType() {
        return OperationType.ENTITY_MULTI_GET;
    }
}