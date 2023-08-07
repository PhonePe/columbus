package com.phonepe.platform.columbus.registry.factory;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * A class representing the different options that can be used to create a registry.
 *
 * @param <K> the type of the key.
 * @param <V> the type of the values.
 */
@Data
@Accessors(fluent = true)
@SuperBuilder(toBuilder = true)
public class RegistryOptions<K, V> {

    /**
     * The base type of the hierarchy whose subtypes are to be added to the registry.
     */
    @NonNull
    private final Class<V> valueType;

    /**
     * The criteria that needs to be met by all the subtypes, along with the requirement of having a
     * {@link RegistryKey} annotation.
     */
    @NonNull
    @Builder.Default
    private final Predicate<Class<? extends V>> typeFilter = Objects::nonNull;

    /**
     * A function that provides the key for a given class and its annotation. A simple string based registry is
     * expected to use the {@link RegistryKey#name()}, and a class based registry is expected to use the
     * {@link RegistryKey#type()}.
     */
    @NonNull
    private final Function<RegistryEntry<? extends V>, K> keyExtractor;

    /**
     * A criteria that needs to be met by all the keys of the registry.
     * <p>
     * {@link RegistryKey} has default values for its fields. Ensure to check that explicitly.
     */
    @NonNull
    @Builder.Default
    private final Predicate<K> keyValidator = Objects::nonNull;

    /**
     * In case there are multiple entries with the same key and highest priority, this determines if the
     * creation should fail. If the conflict is ignored, the registry might choose any of the valid options.
     */
    private final boolean ignorePriorityConflicts;

    public static <K, V> RegistryOptionsBuilder<K, V, ?, ?> builder(
            final Class<V> valueType,
            final Function<RegistryEntry<? extends V>, K> keyExtractor
    ) {
        return new RegistryOptionsBuilderImpl<K, V>()
                .valueType(valueType)
                .keyExtractor(keyExtractor);
    }
}
