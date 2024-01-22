package com.phonepe.commons.columbus.elastic.internal.search.score;

import com.phonepe.commons.columbus.elastic.search.score.ElasticScoreFunctionTransformationContext;
import com.phonepe.commons.columbus.registry.factory.RegistryKey;
import com.phonepe.commons.columbus.search.score.RandomScore;
import org.elasticsearch.index.query.functionscore.ScoreFunctionBuilder;
import org.elasticsearch.index.query.functionscore.ScoreFunctionBuilders;

import static com.phonepe.commons.columbus.common.Predicates.applyIfNotNull;

@RegistryKey(name = RandomScore.TYPE, type = RandomScore.class)
public class RandomScoreToElastic<C extends ElasticScoreFunctionTransformationContext<C>>
        extends AbstractScoreFunctionToElastic<RandomScore, C> {

    @Override
    protected ScoreFunctionBuilder<?> buildScoreFunction(
            final RandomScore scoreFunction,
            final Object props,
            final C context
    ) {
        final var function = ScoreFunctionBuilders.randomFunction();
        applyIfNotNull(scoreFunction.getField(), function::setField);
        applyIfNotNull(scoreFunction.getSeed(), function::seed);
        return function;
    }
}
