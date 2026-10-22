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
package com.phonepe.columbus.internal.registry;

import com.phonepe.columbus.registry.Registry;
import lombok.NoArgsConstructor;

import java.util.Optional;

/**
 * Well, it's empty.
 * <p>
 * Returns empty value for all keys.
 *
 * @param <K> the type of the key.
 * @param <V> the type of the values.
 */
@NoArgsConstructor(staticName = "create")
public class EmptyRegistry<K, V> implements Registry<K, V> {

    @Override
    public Optional<? extends V> get(final K key) {
        return Optional.empty();
    }
}
