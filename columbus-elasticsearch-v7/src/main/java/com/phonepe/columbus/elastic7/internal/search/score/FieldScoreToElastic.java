package com.phonepe.columbus.elastic7.internal.search.score;

import com.phonepe.columbus.elastic7.internal.search.utils.SearchEnums;
import com.phonepe.columbus.elastic7.search.score.ElasticScoreFunctionTransformationContext;
import com.phonepe.columbus.registry.factory.RegistryKey;
import com.phonepe.columbus.search.score.FieldScore;
import org.elasticsearch.index.query.functionscore.ScoreFunctionBuilder;
import org.elasticsearch.index.query.functionscore.ScoreFunctionBuilders;

import static com.phonepe.columbus.common.Predicates.applyIfNotNull;

@RegistryKey(name = FieldScore.TYPE, type = FieldScore.class)
public class FieldScoreToElastic<C extends ElasticScoreFunctionTransformationContext<C>>
        extends AbstractScoreFunctionToElastic<FieldScore, C> {

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
