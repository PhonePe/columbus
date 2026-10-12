package com.phonepe.columbus.dao.node;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.NonNull;

/**
 * An interface representing a routing logic to a part of the index.
 * <p>
 * This could represent partitioning, sharding, replication, etc., be it logical or physical, as defined by the storage.
 */
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "type"
)
public interface NodeId {

    @NonNull
    String getType();
}
