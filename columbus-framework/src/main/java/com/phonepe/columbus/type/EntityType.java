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

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * An interface that represents the type of objects using a name, {@link Class}, {@link java.lang.reflect.Type Type}
 * or {@link com.fasterxml.jackson.core.type.TypeReference TypeReference}.
 * <p>
 * The only serializable child class is {@link NamedType}, and is hence the default for deserialization.
 *
 * @param <T> the type of the class this entity represents.
 */
@JsonDeserialize(as = NamedType.class)
public sealed interface EntityType<T> permits NamedType, JavaType {

    /**
     * A utility for better logging and debugging.
     *
     * @return a string representation of the internal type.
     */
    String name();

    <R, C> R accept(Visitor<T, R, C> visitor, C context);

    /**
     * A visitor that visits an {@link EntityType} of given type.
     *
     * @param <T> the type enclosed by the {@link EntityType} this visitor visits.
     * @param <R> the return type of the visitor.
     * @param <C> the type of context that is passed on to the visitor.
     */
    interface Visitor<T, R, C> {

        R visit(NamedType<T> type, C context);

        R visit(JavaType<T> type, C context);
    }
}
