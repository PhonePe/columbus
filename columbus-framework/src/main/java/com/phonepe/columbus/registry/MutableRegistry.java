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

/**
 * A registry that supports registering values at runtime.
 *
 * @param <K> the type of the key.
 * @param <V> the type of the values.
 */
public interface MutableRegistry<K, V> extends Registry<K, V> {

    /**
     * Registers the value against the given key.
     *
     * @param key   the key for registration.
     * @param value the value being registered.
     */
    void register(K key, V value);
}
