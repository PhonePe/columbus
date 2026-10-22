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
package com.phonepe.columbus.elastic7.internal.search.sort;

import com.phonepe.columbus.elastic7.internal.search.utils.SearchEnums;
import com.phonepe.columbus.elastic7.search.sort.ElasticSortTransformationContext;
import com.phonepe.columbus.elastic7.search.sort.SortToElastic;
import com.phonepe.columbus.search.sort.AbstractSort;
import org.elasticsearch.search.sort.SortBuilder;

import static com.phonepe.columbus.common.Predicates.applyIfNotNull;

public abstract class AbstractSortToElastic<T extends AbstractSort, C extends ElasticSortTransformationContext<C>>
        implements SortToElastic<T, C> {

    @Override
    public SortBuilder<?> visit(final T sort, final Object props, final C context) {
        final var builder = buildSort(sort, props, context);
        applyIfNotNull(sort.getOrder(), SearchEnums::toSortOrder, builder::order);
        return builder;
    }

    @SuppressWarnings("java:S1452")
    protected abstract SortBuilder<?> buildSort(final T sort, final Object props, final C context);
}
