package com.phonepe.columbus.storage.settings;

import com.phonepe.columbus.core.settings.RegistrySettings;
import com.phonepe.columbus.storage.StorageConfiguration;
import com.phonepe.columbus.storage.factory.visitor.StorageClientFactoryContext;
import com.phonepe.columbus.storage.factory.visitor.StorageClientProvider;
import lombok.Data;
import lombok.Singular;
import lombok.experimental.SuperBuilder;

import java.util.Map;

/**
 * A set of parameters to configure storages and their creation.
 */
@Data
@SuperBuilder(toBuilder = true)
public class StorageSettings {

    private final RegistrySettings visitorOverrides;

    private final RegistrySettings commandHandlerOverrides;

    @Singular(ignoreNullCollections = true)
    private final Map<String, StorageClientProvider<
            ? extends StorageConfiguration,
            ? extends StorageClientFactoryContext<?>>> nameOverrides;

    public static StorageSettings defaults() {
        return builder().build();
    }
}
