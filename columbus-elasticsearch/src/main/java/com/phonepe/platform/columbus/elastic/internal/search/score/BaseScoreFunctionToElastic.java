package com.phonepe.platform.columbus.elastic.internal.search.score;

import com.phonepe.platform.columbus.elastic.search.score.ElasticScoreFunctionTransformationContext;
import com.phonepe.platform.columbus.elastic.search.score.ScoreFunctionToElastic;
import com.phonepe.platform.columbus.search.score.BaseScoreFunction;
import org.elasticsearch.index.query.functionscore.FunctionScoreQueryBuilder.FilterFunctionBuilder;
import org.elasticsearch.index.query.functionscore.ScoreFunctionBuilder;

import static com.phonepe.platform.columbus.common.Predicates.applyIfNotNull;

public abstract class BaseScoreFunctionToElastic<T extends BaseScoreFunction,
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
