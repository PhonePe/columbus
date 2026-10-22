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
import com.phonepe.columbus.elastic7.internal.search.utils.SearchEnums;
import com.phonepe.columbus.elastic7.search.query.ElasticQueryTransformationContext;
import com.phonepe.columbus.registry.factory.RegistryKey;
import com.phonepe.columbus.search.query.compound.FunctionScoreQuery;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.functionscore.FunctionScoreQueryBuilder.FilterFunctionBuilder;

import java.util.Optional;

import static com.phonepe.columbus.common.Predicates.applyIfNotNull;

@RegistryKey(name = FunctionScoreQuery.TYPE, type = FunctionScoreQuery.class)
public class FunctionScoreQueryToElastic<C extends ElasticQueryTransformationContext<C>>
        extends AbstractQueryToElastic<FunctionScoreQuery, C> {

    @Override
    protected QueryBuilder buildQuery(final FunctionScoreQuery query, final Object props, final C context) {
        final var scoreFunctionTransformer = context.toolkit().scoreFunctionTransformer();
        final var functions = query.getFunctions().stream()
                .map(scoreFunction -> scoreFunctionTransformer.transform(scoreFunction, props))
                .toArray(FilterFunctionBuilder[]::new);

        final var builder = Optional.ofNullable(query.getQuery())
                .map(innerQuery -> context.transform(innerQuery, props))
                .map(innerQuery -> QueryBuilders.functionScoreQuery(innerQuery, functions))
                .orElseGet(() -> QueryBuilders.functionScoreQuery(functions));

        applyIfNotNull(query.getMaxBoost(), builder::maxBoost);
        applyIfNotNull(query.getMinScore(), builder::setMinScore);
        applyIfNotNull(query.getBoostMode(), SearchEnums::toBoostMode, builder::boostMode);
        applyIfNotNull(query.getScoreMode(), SearchEnums::toScoreMode, builder::scoreMode);

        return builder;
    }
}
