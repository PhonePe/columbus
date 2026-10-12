package com.phonepe.columbus.elastic7.search.score;

import com.phonepe.columbus.search.score.ScoreFunction;
import com.phonepe.columbus.search.score.visitor.ScoreFunctionTransformationVisitor;
import org.elasticsearch.index.query.functionscore.FunctionScoreQueryBuilder.FilterFunctionBuilder;

/**
 * An interface to transform a specific {@link ScoreFunction} to its internal representation for ElasticSearch.
 *
 * @param <T> the type of the {@link ScoreFunction} being transformed.
 * @param <C> the type of the context required for transformation.
 */
public interface ScoreFunctionToElastic<T extends ScoreFunction, C extends ElasticScoreFunctionTransformationContext<C>>
        extends ScoreFunctionTransformationVisitor<FilterFunctionBuilder, T, Object, C> {
}
