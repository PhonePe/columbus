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

import java.util.Optional;

/**
 * An object that provides a way to register some values against some unique keys, and fetch them.
 * <p>
 * This is very similar to {@link java.util.Map Map}s, but is focused more on configurability than storage efficiency.
 *
 * @param <K> the type of the key.
 * @param <V> the type of the values.
 */
public interface Registry<K, V> {

    /**
     * Finds the value that is registered for a given key.
     *
     * @param key the key whose value needs to be fetched.
     * @return the value registered for the key, or empty if no value is registered.
     */
    @SuppressWarnings("java:S1452")
    Optional<? extends V> get(K key);
}
