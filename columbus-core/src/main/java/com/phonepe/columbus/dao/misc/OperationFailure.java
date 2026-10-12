package com.phonepe.columbus.dao.misc;

import lombok.Data;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;

/**
 * Represents a failure while performing a storage operation.
 */
@Data
@SuperBuilder(toBuilder = true)
public class OperationFailure {

    @NonNull
    private final String message;

    private final Throwable exception;
}
