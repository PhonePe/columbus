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

import co.elastic.clients.elasticsearch._types.query_dsl.FunctionScoreQuery.Builder;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryVariant;
import com.phonepe.columbus.elastic9.internal.search.query.AbstractQueryToElastic;
import com.phonepe.columbus.elastic9.internal.search.utils.SearchEnums;
import com.phonepe.columbus.elastic9.search.query.ElasticQueryTransformationContext;
import com.phonepe.columbus.registry.factory.RegistryKey;
import com.phonepe.columbus.search.query.compound.FunctionScoreQuery;

import java.util.Optional;

import static com.phonepe.columbus.common.Predicates.applyIfNotNull;

@RegistryKey(name = FunctionScoreQuery.TYPE, type = FunctionScoreQuery.class)
public class FunctionScoreQueryToElastic<C extends ElasticQueryTransformationContext<C>>
        extends AbstractQueryToElastic<FunctionScoreQuery, C> {

    @Override
    public QueryVariant visit(final FunctionScoreQuery query, final Object props, final C context) {
        final var scoreFunctionTransformer = context.toolkit().scoreFunctionTransformer();
        final var functions = query.getFunctions().stream()
                .map(scoreFunction -> scoreFunctionTransformer.transform(scoreFunction, props))
                .toList();

        final var builder = new Builder().functions(functions);
        applyCommonProperties(query, builder);
        Optional.ofNullable(query.getQuery()).ifPresent(innerQuery ->
                builder.query(context.transform(innerQuery, props)));

        applyIfNotNull(query.getMaxBoost(), Float::doubleValue, builder::maxBoost);
        applyIfNotNull(query.getMinScore(), Float::doubleValue, builder::minScore);
        applyIfNotNull(query.getBoostMode(), SearchEnums::toBoostMode, builder::boostMode);
        applyIfNotNull(query.getScoreMode(), SearchEnums::toScoreMode, builder::scoreMode);

        return builder.build();
    }
}
