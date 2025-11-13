package com.phonepe.commons.columbus.elastic9.internal.search.score;

import co.elastic.clients.elasticsearch._types.query_dsl.FunctionScore;
import co.elastic.clients.elasticsearch._types.query_dsl.RandomScoreFunction;
import com.phonepe.commons.columbus.elastic9.search.score.ElasticScoreFunctionTransformationContext;
import com.phonepe.commons.columbus.registry.factory.RegistryKey;
import com.phonepe.commons.columbus.search.score.WeightScore;

@RegistryKey(name = WeightScore.TYPE, type = WeightScore.class)
public class WeightScoreToElastic<C extends ElasticScoreFunctionTransformationContext<C>>
        extends AbstractScoreFunctionToElastic<WeightScore, C> {

    @Override
    protected FunctionScore.Builder.ContainerBuilder buildFunctionScore(
            final WeightScore scoreFunction,
            final Object props,
            final C context
    ) {
        final var builder = new FunctionScore.Builder();
        // A workaround to set weight and return a ContainerBuilder. Elastic 9 client will not
        // add random_score to output json if it is null.
        return builder.randomScore((RandomScoreFunction) null);
    }
}
