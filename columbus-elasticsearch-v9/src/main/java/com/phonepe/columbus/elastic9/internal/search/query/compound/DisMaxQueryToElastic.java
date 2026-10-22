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
package com.phonepe.columbus.elastic9.internal.search.query.compound;

import co.elastic.clients.elasticsearch._types.query_dsl.DisMaxQuery.Builder;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryVariant;
import com.phonepe.columbus.elastic9.internal.search.query.AbstractQueryToElastic;
import com.phonepe.columbus.elastic9.search.query.ElasticQueryTransformationContext;
import com.phonepe.columbus.registry.factory.RegistryKey;
import com.phonepe.columbus.search.query.compound.DisMaxQuery;

import static com.phonepe.columbus.common.Predicates.applyIfNotNull;

@RegistryKey(name = DisMaxQuery.TYPE, type = DisMaxQuery.class)
public class DisMaxQueryToElastic<C extends ElasticQueryTransformationContext<C>>
        extends AbstractQueryToElastic<DisMaxQuery, C> {

    @Override
    public QueryVariant visit(final DisMaxQuery query, final Object props, final C context) {
        final var builder = new Builder();
        applyCommonProperties(query, builder);
        query.getQueries().stream()
                .map(innerQuery -> context.transform(innerQuery, props))
                .forEachOrdered(builder::queries);
        applyIfNotNull(query.getTieBreaker(), Float::doubleValue, builder::tieBreaker);
        return builder.build();
    }
}
