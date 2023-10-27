package com.phonepe.platform.columbus.elastic.internal.search.score;

import com.phonepe.platform.columbus.elastic.search.score.ElasticScoreFunctionTransformationContext;
import com.phonepe.platform.columbus.registry.factory.RegistryKey;
import com.phonepe.platform.columbus.search.score.WeightScore;
import org.elasticsearch.index.query.functionscore.ScoreFunctionBuilder;
import org.elasticsearch.index.query.functionscore.WeightBuilder;

@RegistryKey(name = WeightScore.TYPE, type = WeightScore.class)
public class WeightScoreToElastic<C extends ElasticScoreFunctionTransformationContext<C>>
        extends BaseScoreFunctionToElastic<WeightScore, C> {

    @Override
    protected ScoreFunctionBuilder<?> buildScoreFunction(final WeightScore scoreFunction, final C context) {
        return new WeightBuilder();
    }
}
