package com.phonepe.commons.columbus.storage.models.entity;

import com.phonepe.commons.columbus.dao.id.Id;
import com.phonepe.commons.columbus.dao.node.NodeId;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.extern.jackson.Jacksonized;

/**
 * Represents the id and node information of a value in an index.
 */
@Data
@Builder
@Jacksonized
public class EntityId {

    @NonNull
    private final Id id;

    private final NodeId nodeId;
}
