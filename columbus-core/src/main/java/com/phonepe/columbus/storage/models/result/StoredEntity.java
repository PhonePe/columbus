package com.phonepe.columbus.storage.models.result;

import com.phonepe.columbus.dao.id.Id;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

/**
 * Represents a value with its id.
 *
 * @param <T> the type of the value.
 */
@Data
@Builder
public class StoredEntity<T> {

    @NonNull
    private final Id id;

    private final T value;
}
