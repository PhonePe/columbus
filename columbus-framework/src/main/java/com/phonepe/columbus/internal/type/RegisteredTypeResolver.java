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

import com.phonepe.columbus.type.EntityType;
import com.phonepe.columbus.type.JavaType;
import com.phonepe.columbus.type.NamedType;
import com.phonepe.columbus.type.TypeRegistry;
import com.phonepe.columbus.type.TypeResolver;
import lombok.AllArgsConstructor;
import lombok.NonNull;

/**
 * A resolver that uses a {@link TypeRegistry} to find the registered {@link JavaType} for a given name.
 */
@AllArgsConstructor(staticName = "of")
public class RegisteredTypeResolver implements TypeResolver {

    @NonNull
    private final TypeRegistry typeRegistry;

    @Override
    @SuppressWarnings("unchecked")
    public <T> JavaType<T> toJavaType(final EntityType<T> type) {
        if (type instanceof JavaType<T> javaType) {
            return javaType;
        }
        final var name = ((NamedType<T>) type).getName();
        return (JavaType<T>) typeRegistry.get(name)
                .orElseThrow(() -> new IllegalArgumentException("No type registered for name '%s'".formatted(name)));
    }
}
