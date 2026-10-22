/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.phonepe.columbus.registry.factory;

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
