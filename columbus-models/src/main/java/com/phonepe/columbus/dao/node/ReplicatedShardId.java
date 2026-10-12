package com.phonepe.columbus.dao.node;

import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

/**
 * An id representing a specific shard of an index, within a preferred replica.
 */
@Data
@Jacksonized
@SuperBuilder
@JsonTypeName(ReplicatedShardId.TYPE)
public class ReplicatedShardId extends ShardId {

    public static final String TYPE = "REPLICA_SHARD";

    private final String replica;

    @Override
    public @NonNull String getType() {
        return TYPE;
    }
}
