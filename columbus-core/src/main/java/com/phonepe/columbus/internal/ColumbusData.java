package com.phonepe.columbus.internal;

import com.phonepe.columbus.Columbus;
import com.phonepe.columbus.core.toolkit.Toolkit;
import com.phonepe.columbus.storage.StorageClient;
import lombok.Data;
import lombok.NonNull;
import lombok.Singular;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

import java.util.Collection;
import java.util.Map;

/**
 * A concrete implementation of {@link Columbus} ensuring all the fields are initialized with valid values.
 */
@Data
@Accessors(fluent = true)
@SuperBuilder(toBuilder = true)
public class ColumbusData implements Columbus {

    @NonNull
    private final Toolkit toolkit;

    @Singular(ignoreNullCollections = true)
    private final Map<String, StorageClient> storages;

    @Override
    public Collection<StorageClient> storages() {
        return storages.values();
    }

    @Override
    public StorageClient storage(final String name) {
        return storages.get(name);
    }
}
