package com.phonepe.commons.columbus.dao.misc;

import com.phonepe.commons.columbus.dao.id.Id;
import lombok.Data;
import lombok.experimental.SuperBuilder;

/**
 * Represents a failure while performing an operation on an entity.
 */
@Data
@SuperBuilder(toBuilder = true)
public class EntityOperationFailure extends IndexOperationFailure {

    private final Id id;
}
