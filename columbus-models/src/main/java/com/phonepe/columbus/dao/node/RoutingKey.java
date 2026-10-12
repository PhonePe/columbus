package com.phonepe.columbus.dao.node;

import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Data;
import lombok.NonNull;
import lombok.Singular;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

/**
 * An id with multiple keys to decide the shard to choose.
 */
@Data
@Jacksonized
@SuperBuilder
@JsonTypeName(RoutingKey.TYPE)
public class RoutingKey implements NodeId {

    public static final String TYPE = "ROUTING";

    @Singular(ignoreNullCollections = true)
    private final List<@NonNull String> keys;

    @Override
    public @NonNull String getType() {
        return TYPE;
    }
}
