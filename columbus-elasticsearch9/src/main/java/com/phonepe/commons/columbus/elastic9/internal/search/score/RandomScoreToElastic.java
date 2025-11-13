package com.phonepe.commons.columbus.elastic9.internal.search.score;

import co.elastic.clients.elasticsearch._types.query_dsl.FunctionScore;
import co.elastic.clients.elasticsearch._types.query_dsl.RandomScoreFunction;
import com.phonepe.commons.columbus.elastic9.search.score.ElasticScoreFunctionTransformationContext;
import com.phonepe.commons.columbus.registry.factory.RegistryKey;
import com.phonepe.commons.columbus.search.score.RandomScore;

import static com.phonepe.commons.columbus.common.Predicates.applyIfNotNull;

@RegistryKey(name = RandomScore.TYPE, type = RandomScore.class)
public class RandomScoreToElastic<C extends ElasticScoreFunctionTransformationContext<C>>
        extends AbstractScoreFunctionToElastic<RandomScore, C> {

    @Override
    protected FunctionScore.Builder.ContainerBuilder buildFunctionScore(
            final RandomScore scoreFunction,
            final Object props,
            final C context
    ) {
        final var builder = new FunctionScore.Builder();
        return builder.randomScore(getRandomScoreFunction(scoreFunction));
    }

    private RandomScoreFunction getRandomScoreFunction(final RandomScore scoreFunction) {
        final var builder = new RandomScoreFunction.Builder();
        applyIfNotNull(scoreFunction.getField(), builder::field);
        applyIfNotNull(scoreFunction.getSeed(), String::valueOf, builder::seed);
        return builder.build();
    }
}
