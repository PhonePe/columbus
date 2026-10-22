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
package com.phonepe.columbus.internal.search.filter;

import com.phonepe.columbus.search.filter.Filter;
import com.phonepe.columbus.search.filter.visitor.FilterTransformationVisitor;
import com.phonepe.columbus.search.query.AbstractQuery.AbstractQueryBuilder;
import com.phonepe.columbus.search.query.Query;
import com.phonepe.columbus.search.query.visitor.QueryTransformationContext;

/**
 * A {@link FilterTransformationVisitor} that first transforms the {@link Filter} to a {@link Query} and then uses its
 * visitor to transform to the internal representation. This allows filters to work across storages as long as the
 * queries they transform to are supported by the storage.
 *
 * @param <R> represents the type used by storage.
 * @param <T> the type of the {@link Filter} being transformed.
 * @param <P> represents the additional context required for transformation.
 * @param <C> the type of the context required for transformation.
 */
public abstract class FilterToQuery<R, T extends Filter, P, C extends QueryTransformationContext<R, P, C>>
        implements FilterTransformationVisitor<R, T, P, C> {

    @Override
    public R visit(final T filter, final P props, final C context) {
        return context.transform(toQueryBuilder(filter, props).build(), props);
    }

    @SuppressWarnings("java:S1452")
    protected abstract AbstractQueryBuilder<?, ?> toQueryBuilder(T filter, final P props);
}
