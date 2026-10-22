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
package com.phonepe.columbus.registry;

import lombok.NonNull;

import java.util.List;

/**
 * A registry that uses a collection of registries to fetch the registered value. The registries are expected to have
 * a certain order / hierarchy and the first non-empty value returned is used.
 *
 * @param <K> the type of the key.
 * @param <V> the type of the values.
 */
public interface RegistryChain<K, V> extends Registry<K, V> {

    /**
     * Getter for internal registries.
     *
     * @return list of registries.
     */
    @SuppressWarnings("java:S1452")
    List<@NonNull Registry<? super K, ? extends V>> registries();
}
