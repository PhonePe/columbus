package com.phonepe.platform.columbus.elastic.internal.search.score;

import com.phonepe.platform.columbus.elastic.internal.search.utils.SearchEnums;
import com.phonepe.platform.columbus.elastic.search.score.ElasticScoreFunctionTransformationContext;
import com.phonepe.platform.columbus.registry.factory.RegistryKey;
import com.phonepe.platform.columbus.search.score.FieldScore;
import org.elasticsearch.index.query.functionscore.ScoreFunctionBuilder;
import org.elasticsearch.index.query.functionscore.ScoreFunctionBuilders;

import static com.phonepe.platform.columbus.common.Predicates.applyIfNotNull;

@RegistryKey(name = FieldScore.TYPE, type = FieldScore.class)
public class FieldScoreToElastic<C extends ElasticScoreFunctionTransformationContext<C>>
        extends BaseScoreFunctionToElastic<FieldScore, C> {

    @Override
    protected ScoreFunctionBuilder<?> buildScoreFunction(
            final FieldScore scoreFunction,
            final Object props,
            final C context
    ) {
        final var function = ScoreFunctionBuilders.fieldValueFactorFunction(scoreFunction.getField());
        applyIfNotNull(scoreFunction.getFactor(), function::factor);
        applyIfNotNull(scoreFunction.getMissing(), function::missing);
        applyIfNotNull(scoreFunction.getModifier(), SearchEnums::toModifier, function::modifier);
        return function;
    }
}
