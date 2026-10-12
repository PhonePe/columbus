package com.phonepe.columbus.storage;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;

/**
 * An interface to configure different {@link StorageClient}s.
 * <p>
 * The configurations are expected to have unique {@link #name}s, and are disabled by default.
 */
@Data
@SuperBuilder
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "type"
)
public abstract class StorageConfiguration {

    private final boolean enabled;

    @NonNull
    private final String name;

    @NonNull
    public abstract String getType();
}
