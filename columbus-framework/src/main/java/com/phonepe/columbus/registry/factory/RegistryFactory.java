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

import com.phonepe.columbus.registry.Registry;

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
