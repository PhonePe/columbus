package com.phonepe.commons.columbus.dao.node;

import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

/**
 * An id representing a specific replica of an index.
 */
@Data
@Jacksonized
@SuperBuilder
@JsonTypeName(ReplicaId.TYPE)
public class ReplicaId implements NodeId {

    public static final String TYPE = "REPLICA";

    @NonNull
    private final String replica;

    @Override
    public @NonNull String getType() {
        return TYPE;
    }
}
