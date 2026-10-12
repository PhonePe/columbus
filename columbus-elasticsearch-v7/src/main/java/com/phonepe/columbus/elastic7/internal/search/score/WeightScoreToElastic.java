package com.phonepe.columbus.elastic7.internal.search.score;

import com.phonepe.columbus.elastic7.search.score.ElasticScoreFunctionTransformationContext;
import com.phonepe.columbus.registry.factory.RegistryKey;
import com.phonepe.columbus.search.score.WeightScore;
import org.elasticsearch.index.query.functionscore.ScoreFunctionBuilder;
import org.elasticsearch.index.query.functionscore.WeightBuilder;

@RegistryKey(name = WeightScore.TYPE, type = WeightScore.class)
public class WeightScoreToElastic<C extends ElasticScoreFunctionTransformationContext<C>>
        extends AbstractScoreFunctionToElastic<WeightScore, C> {

    @Override
    protected ScoreFunctionBuilder<?> buildScoreFunction(
            final WeightScore scoreFunction,
            final Object props,
            final C context
    ) {
        return new WeightBuilder();
    }
}
