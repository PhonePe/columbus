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

import java.util.Optional;

/**
 * A registry that allows monitoring the successes and failures of get operations.
 * <p>
 * The {@link Listener}'s {@link MonitoredRegistry.Listener#success(Object, Object) success(key, value)} is called if a
 * value is found for the key, and {@link Listener#failure(Object) failure(key)} is called if no value is found.
 *
 * @param <K> the type of the key.
 * @param <V> the type of the values.
 */
@Builder
public class MonitoredRegistry<K, V> implements Registry<K, V> {

    @NonNull
    private final Registry<? super K, ? extends V> delegate;

    @NonNull
    private final Listener<? super K, ? super V> listener;

    @Override
    public Optional<? extends V> get(final K key) {
        final var value = delegate.get(key);
        if (value.isPresent()) {
            listener.success(key, value.get());
        } else {
            listener.failure(key);
        }
        return value;
    }

    public interface Listener<K, V> {

        void success(K key, V value);

        void failure(K key);
    }
}
