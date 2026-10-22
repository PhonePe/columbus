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
package com.phonepe.columbus.internal.type;

import com.phonepe.columbus.registry.Registry;
import com.phonepe.columbus.type.JavaType;
import com.phonepe.columbus.type.TypeRegistry;
import lombok.AllArgsConstructor;
import lombok.NonNull;

import java.util.Optional;

/**
 * An implementation of {@link TypeRegistry} that delegates the task of fetching the type to a {@link Registry}.
 */
@AllArgsConstructor(staticName = "of")
public class DelegatingTypeRegistry implements TypeRegistry {

    @NonNull
    private final Registry<String, JavaType<?>> delegate;

    @Override
    public Optional<? extends JavaType<?>> get(final String key) {
        return delegate.get(key);
    }
}
