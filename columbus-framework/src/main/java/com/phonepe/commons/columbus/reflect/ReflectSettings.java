package com.phonepe.commons.columbus.reflect;

import lombok.Data;
import lombok.NonNull;
import lombok.Singular;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.Map;

/**
 * A set of parameters to configure Reflect as needed.
 */
@Data
@SuperBuilder
public class ReflectSettings {

    @Singular(ignoreNullCollections = true)
    private final List<@NonNull String> packageNames;

    @Singular(value = "classType", ignoreNullCollections = true)
    private final List<@NonNull Class<?>> classes;

    @Singular(ignoreNullCollections = true)
    private final Map<@NonNull Class<?>, @NonNull Class<?>> mixins;

    private final ReflectionInitializer initializerOverride;

    private final InstanceFactory instanceFactory;

    public static ReflectSettings defaults() {
        return builder().build();
    }
}
