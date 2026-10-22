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
package com.phonepe.columbus.visitor.impl;

import com.phonepe.columbus.visitor.Visitor;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;

/**
 * The visitor context includes all the data that is accessible while visiting a value. This allows passing
 * additional data to all the visitors, and support recursive execution.
 * <p>
 * It uses the {@link #resolver} to identify the visitor that is applicable for a given value and context type. Note
 * that the resolver need not be strictly bound to the type parameters of the context.
 *
 * @param <R> the return type of the visitor.
 * @param <B> the base type the visitor can visit.
 * @param <P> the type of props the visitor supports.
 * @param <C> self reference, the context used by the visitor.
 * @see VisitorResolver
 * @see EntityVisitor
 */
@Getter
@SuperBuilder(toBuilder = true)
public abstract class VisitorContext<R, B, P, C extends VisitorContext<R, B, P, C>> implements Visitor<R, B, P> {

    private final P defaultProps;

    @NonNull
    private final VisitorResolver<? extends R, ? super B, ? super P, ? super C> resolver;

    @Override
    public <T extends B> R visit(final T value, final P props) {
        return resolver.visitorFor(value).visit(value, props, self());
    }

    @Override
    public <T extends B> R visit(final T value) {
        return visit(value, defaultProps);
    }

    @NonNull
    protected abstract C self();
}
