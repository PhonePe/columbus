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
package com.phonepe.columbus.elastic7.internal.search.query.compound;

import com.phonepe.columbus.elastic7.internal.search.query.AbstractQueryToElastic;
import com.phonepe.columbus.elastic7.search.query.ElasticQueryTransformationContext;
import com.phonepe.columbus.registry.factory.RegistryKey;
import com.phonepe.columbus.search.query.compound.BoostingQuery;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

import java.util.Objects;

import static com.phonepe.columbus.common.Predicates.applyIfNotNull;

@RegistryKey(name = BoostingQuery.TYPE, type = BoostingQuery.class)
public class BoostingQueryToElastic<C extends ElasticQueryTransformationContext<C>>
        extends AbstractQueryToElastic<BoostingQuery, C> {

    @Override
    protected QueryBuilder buildQuery(final BoostingQuery query, final Object props, final C context) {
        final var positive = Objects.requireNonNull(query.getPositive(), "positive must not be null");
        final var negative = Objects.requireNonNull(query.getNegative(), "negative must not be null");
        final var builder = QueryBuilders.boostingQuery(
                context.transform(positive, props),
                context.transform(negative, props));
        applyIfNotNull(query.getNegativeBoost(), builder::negativeBoost);
        return builder;
    }
}
