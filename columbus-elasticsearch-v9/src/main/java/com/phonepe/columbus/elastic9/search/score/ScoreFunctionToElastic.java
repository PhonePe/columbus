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
package com.phonepe.columbus.elastic9.search.score;

import co.elastic.clients.elasticsearch._types.query_dsl.FunctionScore;
import com.phonepe.columbus.search.score.ScoreFunction;
import com.phonepe.columbus.search.score.visitor.ScoreFunctionTransformationVisitor;

/**
 * An interface to transform a specific {@link ScoreFunction} to its internal representation for ElasticSearch.
 *
 * @param <T> the type of the {@link ScoreFunction} being transformed.
 * @param <C> the type of the context required for transformation.
 */
public interface ScoreFunctionToElastic<T extends ScoreFunction, C extends ElasticScoreFunctionTransformationContext<C>>
        extends ScoreFunctionTransformationVisitor<FunctionScore, T, Object, C> {
}
