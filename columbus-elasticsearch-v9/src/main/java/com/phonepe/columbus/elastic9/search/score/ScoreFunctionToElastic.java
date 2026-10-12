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
