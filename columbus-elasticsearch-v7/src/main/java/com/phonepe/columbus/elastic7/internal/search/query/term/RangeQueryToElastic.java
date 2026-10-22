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
package com.phonepe.columbus.elastic7.internal.search.query.term;

import com.phonepe.columbus.elastic7.internal.search.query.AbstractQueryToElastic;
import com.phonepe.columbus.elastic7.search.query.ElasticQueryTransformationContext;
import com.phonepe.columbus.registry.factory.RegistryKey;
import com.phonepe.columbus.search.query.term.RangeQuery;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;

import static com.phonepe.columbus.common.Predicates.applyIfNotNull;

@RegistryKey(name = RangeQuery.TYPE, type = RangeQuery.class)
public class RangeQueryToElastic<C extends ElasticQueryTransformationContext<C>>
        extends AbstractQueryToElastic<RangeQuery, C> {

    @Override
    protected QueryBuilder buildQuery(final RangeQuery query, final Object props, final C context) {
        final var builder = QueryBuilders.rangeQuery(query.getField())
                .includeLower(query.includeLower())
                .includeUpper(query.includeUpper());

        applyIfNotNull(query.getFrom(), builder::from);
        applyIfNotNull(query.getTo(), builder::to);
        applyIfNotNull(query.getTimezone(), builder::timeZone);
        applyIfNotNull(query.getFormat(), builder::format);
        applyIfNotNull(query.getRelation(), builder::relation);

        return builder;
    }
}
