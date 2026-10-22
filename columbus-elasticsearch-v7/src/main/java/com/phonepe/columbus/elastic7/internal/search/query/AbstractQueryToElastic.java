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
package com.phonepe.columbus.elastic7.internal.search.query;

import com.phonepe.columbus.elastic7.search.query.ElasticQueryTransformationContext;
import com.phonepe.columbus.elastic7.search.query.QueryToElastic;
import com.phonepe.columbus.search.query.AbstractQuery;
import org.elasticsearch.index.query.QueryBuilder;

import static com.phonepe.columbus.common.Predicates.applyIfNotNull;

public abstract class AbstractQueryToElastic<T extends AbstractQuery, C extends ElasticQueryTransformationContext<C>>
        implements QueryToElastic<T, C> {

    @Override
    public QueryBuilder visit(final T query, final Object props, final C context) {
        final var builder = buildQuery(query, props, context);
        applyIfNotNull(query.getName(), builder::queryName);
        applyIfNotNull(query.getBoost(), builder::boost);
        return builder;
    }

    protected abstract QueryBuilder buildQuery(final T query, final Object props, final C context);
}
