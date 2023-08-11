package com.phonepe.platform.columbus.core.settings;

import com.phonepe.platform.columbus.core.toolkit.ToolkitInitializer;
import com.phonepe.platform.columbus.core.toolkit.ToolkitSettings;
import com.phonepe.platform.columbus.storage.settings.StorageSettings;
import com.phonepe.platform.columbus.type.TypeRegistry;
import lombok.Data;
import lombok.experimental.SuperBuilder;

/**
 * A set of parameters to configure {@link com.phonepe.platform.columbus.Columbus Columbus}.
 *
 * @see ColumbusConfiguration
 */
@Data
@SuperBuilder(toBuilder = true)
public class ColumbusSettings {

    private final ToolkitSettings toolkitSettings;

    private final ToolkitInitializer toolkitInitializer;

    private final TypeRegistry typeRegistry;

    private final StorageSettings storageSettings;

    public static ColumbusSettings defaults() {
        return builder().build();
    }
}
