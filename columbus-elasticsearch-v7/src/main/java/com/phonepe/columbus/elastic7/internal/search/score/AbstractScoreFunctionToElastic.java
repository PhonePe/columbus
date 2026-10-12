package com.phonepe.columbus.elastic7.internal.search.score;

import com.phonepe.columbus.elastic7.search.score.ElasticScoreFunctionTransformationContext;
import com.phonepe.columbus.elastic7.search.score.ScoreFunctionToElastic;
import com.phonepe.columbus.search.score.AbstractScoreFunction;
import org.elasticsearch.index.query.functionscore.FunctionScoreQueryBuilder.FilterFunctionBuilder;
import org.elasticsearch.index.query.functionscore.ScoreFunctionBuilder;

import static com.phonepe.columbus.common.Predicates.applyIfNotNull;

public abstract class AbstractScoreFunctionToElastic<T extends AbstractScoreFunction,
        C extends ElasticScoreFunctionTransformationContext<C>>
        implements ScoreFunctionToElastic<T, C> {

    @Override
    public FilterFunctionBuilder visit(final T scoreFunction, final Object props, final C context) {
        final var function = buildScoreFunction(scoreFunction, props, context);
        applyIfNotNull(scoreFunction.getWeight(), function::setWeight);
        if (scoreFunction.getQuery() == null) {
            return new FilterFunctionBuilder(function);
        }
        final var query = context.toolkit().queryTransformer().transform(scoreFunction.getQuery(), props);
        return new FilterFunctionBuilder(query, function);
    }

    @SuppressWarnings("java:S1452")
    protected abstract ScoreFunctionBuilder<?> buildScoreFunction(
            final T scoreFunction,
            final Object props,
            final C context
    );
}
