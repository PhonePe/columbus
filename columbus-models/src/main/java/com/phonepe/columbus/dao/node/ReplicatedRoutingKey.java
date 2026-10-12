package com.phonepe.columbus.dao.node;

import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

/**
 * An id with multiple keys to decide the shard to choose, within an optional replica.
 */
@Data
@Jacksonized
@SuperBuilder
@JsonTypeName(ReplicatedRoutingKey.TYPE)
public class ReplicatedRoutingKey extends RoutingKey {

    public static final String TYPE = "REPLICA_ROUTING";

    private final String replica;

    @Override
    public @NonNull String getType() {
        return TYPE;
    }
}
