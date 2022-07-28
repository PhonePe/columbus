package com.phonepe.platform.columbus.core.models.operations;


import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

import java.util.HashMap;

@Jacksonized
@SuperBuilder
@Data
@EqualsAndHashCode(callSuper = true)
public class CreateMappingOperation extends StorageOperation {
    @NonNull
    private HashMap<String, Object> mappings;

    @Override
    protected @NonNull OperationType getOperationType() {
        return OperationType.CREATE_MAPPING;
    }
}
