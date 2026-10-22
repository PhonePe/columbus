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
package com.phonepe.columbus.elastic9.internal.search.score;

import co.elastic.clients.elasticsearch._types.query_dsl.FunctionScore;
import com.phonepe.columbus.elastic9.search.score.ElasticScoreFunctionTransformationContext;
import com.phonepe.columbus.elastic9.search.score.ScoreFunctionToElastic;
import com.phonepe.columbus.search.score.AbstractScoreFunction;

import static com.phonepe.columbus.common.Predicates.applyIfNotNull;

public abstract class AbstractScoreFunctionToElastic<T extends AbstractScoreFunction,
        C extends ElasticScoreFunctionTransformationContext<C>>
        implements ScoreFunctionToElastic<T, C> {

    @Override
    public FunctionScore visit(final T scoreFunction, final Object props, final C context) {
        final var builder = buildFunctionScore(scoreFunction, props, context);
        applyIfNotNull(scoreFunction.getWeight(), Float::doubleValue, builder::weight);
        if (scoreFunction.getQuery() == null) {
            return builder.build();
        }
        final var query = context.toolkit().queryTransformer().transform(scoreFunction.getQuery(), props);
        return builder.filter(query).build();
    }

    @SuppressWarnings("java:S1452")
    protected abstract FunctionScore.Builder.ContainerBuilder buildFunctionScore(
            final T scoreFunction,
            final Object props,
            final C context
    );
}
