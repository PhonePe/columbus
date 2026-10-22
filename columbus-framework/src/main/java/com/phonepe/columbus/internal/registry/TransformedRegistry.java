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
import java.util.function.Function;

/**
 * A registry that first transforms the given key, uses it to fetch the results, and then transforms it to the
 * desired type.
 *
 * @param <K> the type of the key before transformation.
 * @param <T> the type of the keys in the {@link #registry}.
 * @param <R> the type of the values in the {@link #registry}.
 * @param <V> the type of the values after transformation.
 */
@Builder
public class TransformedRegistry<K, T, R, V> implements Registry<K, V> {

    @NonNull
    private final Function<? super K, ? extends T> keyTransformer;

    @NonNull
    private final Registry<? super T, ? extends R> registry;

    @NonNull
    private final Function<? super R, ? extends V> valueTransformer;

    @Override
    public Optional<? extends V> get(final K key) {
        return registry.get(keyTransformer.apply(key)).map(valueTransformer);
    }
}
