package com.phonepe.columbus.elastic9.internal.search.score;

import co.elastic.clients.elasticsearch._types.query_dsl.FunctionScore;
import co.elastic.clients.elasticsearch._types.query_dsl.ScriptScoreFunction;
import com.phonepe.columbus.elastic9.internal.search.utils.MiscTransforms;
import com.phonepe.columbus.elastic9.search.score.ElasticScoreFunctionTransformationContext;
import com.phonepe.columbus.registry.factory.RegistryKey;
import com.phonepe.columbus.search.score.ScriptScore;

import java.util.Objects;

@RegistryKey(name = ScriptScore.TYPE, type = ScriptScore.class)
public class ScriptScoreToElastic<C extends ElasticScoreFunctionTransformationContext<C>>
        extends AbstractScoreFunctionToElastic<ScriptScore, C> {

    @Override
    protected FunctionScore.Builder.ContainerBuilder buildFunctionScore(
            final ScriptScore scoreFunction,
            final Object props,
            final C context
    ) {
        final var builder = new FunctionScore.Builder();

        final var script = Objects.requireNonNull(scoreFunction.getScript(), "script must not be null");

        final var scriptScoreFunction = new ScriptScoreFunction.Builder()
                .script(MiscTransforms.toElastic(script))
                .build();

        return builder.scriptScore(scriptScoreFunction);
    }
}
