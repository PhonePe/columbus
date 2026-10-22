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
import lombok.Builder;
import lombok.NonNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * A registry that caches the results from the delegate.
 * <p>
 * For any new key, the result is fetched from the {@link CachedRegistry#delegate}, and stored for all future gets.
 *
 * @param <K> the type of the key.
 * @param <V> the type of the values.
 */
@Builder
public class CachedRegistry<K, V> implements Registry<K, V> {

    @NonNull
    @Builder.Default
    private final Map<K, Optional<? extends V>> entries = new HashMap<>();

    @NonNull
    private final Registry<? super K, ? extends V> delegate;

    @Override
    public Optional<? extends V> get(final K key) {
        return entries.computeIfAbsent(key, delegate::get);
    }
}
