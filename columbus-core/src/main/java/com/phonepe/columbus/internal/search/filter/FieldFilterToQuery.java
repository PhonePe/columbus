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

import com.phonepe.columbus.search.filter.FieldFilter;
import com.phonepe.columbus.search.query.AbstractQuery.AbstractQueryBuilder;
import com.phonepe.columbus.search.query.FieldQuery.FieldQueryBuilder;
import com.phonepe.columbus.search.query.visitor.QueryTransformationContext;

public abstract class FieldFilterToQuery<R, T extends FieldFilter, P, C extends QueryTransformationContext<R, P, C>>
        extends FilterToQuery<R, T, P, C> {

    @Override
    protected AbstractQueryBuilder<?, ?> toQueryBuilder(final T filter, final P props) {
        return toFieldQueryBuilder(filter, props)
                .field(filter.getField());
    }

    @SuppressWarnings("java:S1452")
    protected abstract FieldQueryBuilder<?, ?> toFieldQueryBuilder(T filter, final P props);
}
