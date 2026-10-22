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
package com.phonepe.columbus.search.filter.visitor;

import com.phonepe.columbus.search.filter.Filter;
import com.phonepe.columbus.search.query.visitor.QueryTransformationContext;
import com.phonepe.columbus.search.query.visitor.QueryTransformationVisitor;

/**
 * An interface to transform a specific {@link Filter} to the internal query used by the storage.
 *
 * @param <R> represents the type used by storage.
 * @param <T> the type of the {@link Filter} being transformed.
 * @param <P> represents the additional context required for transformation.
 * @param <C> the type of the context required for transformation.
 */
public interface FilterTransformationVisitor<R, T extends Filter, P, C extends QueryTransformationContext<R, P, C>>
        extends QueryTransformationVisitor<R, T, P, C> {

    @Override
    R visit(final T filter, P props, final C context);
}
