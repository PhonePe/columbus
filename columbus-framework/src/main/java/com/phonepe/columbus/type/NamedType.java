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

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

/**
 * A type of {@link EntityType} that uses a simple string name to identify a type.
 * <p>
 * {@link TypeResolver} can be used to find the {@link JavaType} corresponding to the given name.
 *
 * @param <T> the type of the class this entity represents.
 * @see TypeResolver
 */
@Data
@AllArgsConstructor(staticName = "of", onConstructor_ = @JsonCreator)
public final class NamedType<T> implements EntityType<T> {

    @NonNull
    @JsonValue
    private final String name;

    @Override
    public String name() {
        return name;
    }

    @Override
    public <R, C> R accept(final Visitor<T, R, C> visitor, final C context) {
        return visitor.visit(this, context);
    }
}
