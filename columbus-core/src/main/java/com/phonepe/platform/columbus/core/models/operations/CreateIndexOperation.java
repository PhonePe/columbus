package com.phonepe.platform.columbus.core.models.operations;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@Jacksonized
@SuperBuilder
@Data
@EqualsAndHashCode(callSuper = true)
public class CreateIndexOperation extends StorageOperation {
    @Override
    protected @NonNull OperationType getOperationType() {
        return OperationType.CREATE_INDEX;
    }
}
