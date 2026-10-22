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

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.Accessors;

/**
 * A holder for the annotation and the annotated class. This class is used by {@link RegistryFactory} to allow
 * extraction of the key for the registry. {@link RegistryOptions} allows customizing how the key is extracted.
 *
 * @param <T> the type of the annotated class.
 */
@Data
@Accessors(fluent = true)
@AllArgsConstructor(staticName = "of")
public final class RegistryEntry<T> {

    @NonNull
    private final RegistryKey key;

    @NonNull
    private final Class<T> value;

    public String name() {
        return this.key.name();
    }

    public Class<?> type() {
        return this.key.type();
    }

    public int priority() {
        return this.key.priority();
    }
}
