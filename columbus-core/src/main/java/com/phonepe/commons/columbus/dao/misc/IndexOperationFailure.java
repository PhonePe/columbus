package com.phonepe.commons.columbus.dao.misc;

import com.phonepe.commons.columbus.dao.index.Index;
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
