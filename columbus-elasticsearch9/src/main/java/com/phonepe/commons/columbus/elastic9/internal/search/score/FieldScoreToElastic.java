package com.phonepe.commons.columbus.elastic9.internal.search.score;

import co.elastic.clients.elasticsearch._types.query_dsl.FieldValueFactorScoreFunction;
import co.elastic.clients.elasticsearch._types.query_dsl.FunctionScore;
import com.phonepe.commons.columbus.elastic9.internal.search.utils.SearchEnums;
import com.phonepe.commons.columbus.elastic9.search.score.ElasticScoreFunctionTransformationContext;
import com.phonepe.commons.columbus.registry.factory.RegistryKey;
import com.phonepe.commons.columbus.search.score.FieldScore;

import static com.phonepe.commons.columbus.common.Predicates.applyIfNotNull;

@RegistryKey(name = FieldScore.TYPE, type = FieldScore.class)
public class FieldScoreToElastic<C extends ElasticScoreFunctionTransformationContext<C>>
        extends AbstractScoreFunctionToElastic<FieldScore, C> {

    @Override
    protected FunctionScore.Builder.ContainerBuilder buildFunctionScore(
            final FieldScore scoreFunction,
            final Object props,
            final C context
    ) {
        final var builder = new FunctionScore.Builder();
        return builder.fieldValueFactor(getFieldValueFactorScoreFunction(scoreFunction));
    }

    private FieldValueFactorScoreFunction getFieldValueFactorScoreFunction(final FieldScore scoreFunction) {
        final var builder = new FieldValueFactorScoreFunction.Builder()
                .field(scoreFunction.getField());
        applyIfNotNull(scoreFunction.getFactor(), Float::doubleValue, builder::factor);
        applyIfNotNull(scoreFunction.getMissing(), Float::doubleValue, builder::missing);
        applyIfNotNull(scoreFunction.getModifier(), SearchEnums::toModifier, builder::modifier);
        return builder.build();
    }
}
