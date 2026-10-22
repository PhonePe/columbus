/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.phonepe.columbus.elastic7.internal.search.score;

import com.phonepe.columbus.elastic7.search.score.ElasticScoreFunctionTransformationContext;
import com.phonepe.columbus.registry.factory.RegistryKey;
import com.phonepe.columbus.search.score.DecayScore;
import org.elasticsearch.index.query.functionscore.DecayFunctionBuilder;
import org.elasticsearch.index.query.functionscore.ScoreFunctionBuilder;
import org.elasticsearch.index.query.functionscore.ScoreFunctionBuilders;

import java.util.Locale;
import java.util.Objects;

@RegistryKey(name = DecayScore.TYPE, type = DecayScore.class)
public class DecayScoreToElastic<C extends ElasticScoreFunctionTransformationContext<C>>
        extends AbstractScoreFunctionToElastic<DecayScore, C> {

    @Override
    protected ScoreFunctionBuilder<?> buildScoreFunction(
            final DecayScore scoreFunction,
            final Object props,
            final C context
    ) {
        return getBuilder(scoreFunction.getMode()).build(
                scoreFunction.getField(),
                scoreFunction.getOrigin(),
                scoreFunction.getScale(),
                scoreFunction.getOffset(),
                Objects.requireNonNullElse(scoreFunction.getDecay(), DecayFunctionBuilder.DEFAULT_DECAY)
        );
    }

    private static DecayFunction getBuilder(final String mode) {
        return switch (mode.toLowerCase(Locale.ROOT)) {
            case "linear" -> ScoreFunctionBuilders::linearDecayFunction;
            case "gauss" -> ScoreFunctionBuilders::gaussDecayFunction;
            case "exponential" -> ScoreFunctionBuilders::exponentialDecayFunction;
            default -> throw new IllegalArgumentException("Unsupported decay score mode: " + mode);
        };
    }

    private interface DecayFunction {

        @SuppressWarnings("java:S1452")
        DecayFunctionBuilder<?> build(
                final String field,
                final Object origin,
                final Object scale,
                final Object offset,
                final double decay
        );
    }
}
