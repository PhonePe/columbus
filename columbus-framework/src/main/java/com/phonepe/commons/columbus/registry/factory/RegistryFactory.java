package com.phonepe.commons.columbus.registry.factory;

import com.phonepe.commons.columbus.registry.Registry;

/**
 * A factory to create a registry using the {@link RegistryKey} annotations on the subtypes of a class.
 */
public interface RegistryFactory {

    /**
     * Create a registry conforming to the options provided.
     *
     * @param options the configurations for creating the registry.
     * @param <K>     the type of the key.
     * @param <V>     the type of the values.
     * @return a registry of entries matching all the criteria specified by {@code options}.
     * @throws IllegalArgumentException if there are conflicting types with the same key and highest priority.
     * @implSpec The implementations are expected to follow the following steps, or have a similar effect:
     * <ol>
     *   <li>
     *     Get all the subtypes of {@link RegistryOptions#valueType()} which have the {@link RegistryKey} annotation.
     *   </li>
     *   <li>
     *     Filter the subtypes by {@link RegistryOptions#typeFilter()}.
     *   </li>
     *   <li>
     *     Group the entries by the key provided by {@link RegistryOptions#keyExtractor()}, which meet the criteria
     *     specified by {@link RegistryOptions#keyValidator()}.
     *   </li>
     *   <li>
     *     Pick the entry with highest {@link RegistryKey#priority()} in each group. Check for conflicts as specified
     *     by {@link RegistryOptions#ignorePriorityConflicts()}.
     *   </li>
     *   <li>
     *     Create a registry with the keys and the instances of the annotated class.
     *   </li>
     * </ol>
     * @see RegistryKey
     * @see RegistryOptions
     */
    <K, V> Registry<K, V> create(RegistryOptions<K, V> options);
}
