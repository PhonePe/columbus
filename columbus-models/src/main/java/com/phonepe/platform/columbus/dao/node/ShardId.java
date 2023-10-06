package com.phonepe.platform.columbus.dao.node;

import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

/**
 * An id representing a specific shard of an index.
 */
@Data
@Jacksonized
@SuperBuilder
@JsonTypeName(ShardId.TYPE)
public class ShardId implements NodeId {

    public static final String TYPE = "SHARD";

    @NonNull
    private final String shard;

    @Override
    public @NonNull String getType() {
        return TYPE;
    }
}
