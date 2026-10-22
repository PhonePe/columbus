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
package com.phonepe.columbus.internal.visitor;

import com.phonepe.columbus.registry.Registry;
import com.phonepe.columbus.visitor.impl.EntityVisitor;
import com.phonepe.columbus.visitor.impl.VisitorContext;
import com.phonepe.columbus.visitor.impl.VisitorResolver;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;

/**
 * A visitor resolver that uses a {@link Registry} to get a visitor for the given value.
 */
@Getter
@SuperBuilder
public class VisitorRegistryResolver<R, B, P, C extends VisitorContext<R, B, P, C>>
        implements VisitorResolver<R, B, P, C> {

    @NonNull
    private final Registry<? super B, ? extends EntityVisitor<? extends R, ? extends B, B, ? super P, ? super C>>
            registry;

    @Override
    public @NonNull <T extends B> EntityVisitor<? extends R, T, ? super B, ? super P, ? super C> visitorFor(final T value) {
        return (EntityVisitor<? extends R, T, ? super B, ? super P, ? super C>) registry.get(value)
                .orElseThrow(() -> new IllegalArgumentException("No visitor registered for value: " + value));
    }
}
