package com.phonepe.columbus.dao.misc;

import com.phonepe.columbus.dao.index.Index;
import lombok.Data;
import lombok.experimental.SuperBuilder;

/**
 * Represents a failure while performing an operation on an index.
 */
@Data
@SuperBuilder(toBuilder = true)
public class IndexOperationFailure extends OperationFailure {

    private final Index index;
}
