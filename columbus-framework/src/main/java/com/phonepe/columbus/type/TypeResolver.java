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
package com.phonepe.columbus.type;

import java.lang.reflect.Type;

/**
 * A helper to resolve an {@link EntityType} to a {@link JavaType}.
 */
public interface TypeResolver {

    /**
     * Get the {@link JavaType} representing the given {@link EntityType}.
     *
     * @param type the type to be converted.
     * @param <T>  the type represented by the input.
     * @return {@code type} if it is already a {@link JavaType}, resolve a matching {@link JavaType} otherwise.
     * @throws IllegalArgumentException if the {@code type} is a {@link NamedType} and no {@link JavaType} is
     *                                  registered for that name.
     */
    <T> JavaType<T> toJavaType(EntityType<T> type);

    /**
     * Get the raw {@link Type} representing the given {@link EntityType}.
     * @param type the type to be converted.
     * @return the raw {@link Type} type.
     */
    default Type toRawType(EntityType<?> type) {
        return toJavaType(type).getType().getType();
    }
}
