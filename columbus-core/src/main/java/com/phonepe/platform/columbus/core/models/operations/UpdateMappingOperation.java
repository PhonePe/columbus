package com.phonepe.platform.columbus.core.models.operations;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

import javax.validation.constraints.NotNull;
import java.util.HashMap;


@Jacksonized
@SuperBuilder
@Data
@EqualsAndHashCode(callSuper = true)
public class UpdateMappingOperation extends StorageOperation {
    @NotNull
    @NonNull
    private HashMap<String, Object> mappings;

    @Override
    protected @NonNull OperationType getOperationType() {
        return OperationType.UPDATE_MAPPING;
    }
}
