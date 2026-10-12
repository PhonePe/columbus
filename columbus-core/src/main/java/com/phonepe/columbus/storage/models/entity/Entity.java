package com.phonepe.columbus.storage.models.entity;

import com.phonepe.columbus.dao.id.Id;
import com.phonepe.columbus.dao.node.NodeId;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.extern.jackson.Jacksonized;

/**
 * Represents a value in an index with its id and node.
 *
 * @param <T> the type of the value.
 */
@Data
@Builder
@Jacksonized
public class Entity<T> {

    @NonNull
    private final Id id;

    private final NodeId nodeId;

    private final T value;
}
