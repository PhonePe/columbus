package com.phonepe.commons.columbus.elastic.internal.search.score;

import com.phonepe.commons.columbus.elastic.search.score.ElasticScoreFunctionTransformationContext;
import com.phonepe.commons.columbus.registry.factory.RegistryKey;
import com.phonepe.commons.columbus.search.score.WeightScore;
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
