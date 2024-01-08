package com.phonepe.commons.columbus.elastic.internal.search.score;

import com.phonepe.commons.columbus.elastic.internal.search.utils.MiscTransforms;
import com.phonepe.commons.columbus.elastic.search.score.ElasticScoreFunctionTransformationContext;
import com.phonepe.commons.columbus.registry.factory.RegistryKey;
import com.phonepe.commons.columbus.search.score.ScriptScore;
import org.elasticsearch.index.query.functionscore.ScoreFunctionBuilder;
import org.elasticsearch.index.query.functionscore.ScoreFunctionBuilders;

import java.util.Objects;

@RegistryKey(name = ScriptScore.TYPE, type = ScriptScore.class)
public class ScriptScoreToElastic<C extends ElasticScoreFunctionTransformationContext<C>>
        extends BaseScoreFunctionToElastic<ScriptScore, C> {

    @Override
    protected ScoreFunctionBuilder<?> buildScoreFunction(
            final ScriptScore scoreFunction,
            final Object props,
            final C context
    ) {
        final var script = Objects.requireNonNull(scoreFunction.getScript(), "script must not be null");
        return ScoreFunctionBuilders.scriptFunction(MiscTransforms.toElastic(script));
    }
}
