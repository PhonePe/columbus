package com.phonepe.platform.columbus.core.settings;

import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.util.function.Predicate;

/**
 * A class with some of the default options to use to create registries.
 */
@Data
@SuperBuilder(toBuilder = true)
public class RegistrySettings {

    private final Predicate<Class<?>> defaultTypeFilter;

    private final Predicate<String> defaultNameValidator;

    private final Predicate<Class<?>> defaultTypeValidator;

    public static RegistrySettings defaults() {
        return builder().build();
    }
}
