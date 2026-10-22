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
package com.phonepe.columbus.elastic9.internal.search.score;

import co.elastic.clients.elasticsearch._types.GeoLocation;
import co.elastic.clients.elasticsearch._types.Time;
import co.elastic.clients.elasticsearch._types.query_dsl.DateDecayFunction;
import co.elastic.clients.elasticsearch._types.query_dsl.DecayFunction;
import co.elastic.clients.elasticsearch._types.query_dsl.DecayFunctionBuilders;
import co.elastic.clients.elasticsearch._types.query_dsl.DecayPlacement;
import co.elastic.clients.elasticsearch._types.query_dsl.FunctionScore;
import co.elastic.clients.elasticsearch._types.query_dsl.FunctionScore.Builder;
import co.elastic.clients.elasticsearch._types.query_dsl.GeoDecayFunction;
import co.elastic.clients.elasticsearch._types.query_dsl.NumericDecayFunction;
import co.elastic.clients.util.ObjectBuilder;
import com.phonepe.columbus.elastic9.internal.search.utils.MiscTransforms;
import com.phonepe.columbus.elastic9.search.score.ElasticScoreFunctionTransformationContext;
import com.phonepe.columbus.registry.factory.RegistryKey;
import com.phonepe.columbus.search.score.DecayScore;

import java.util.Locale;

import static com.phonepe.columbus.common.Predicates.applyIfNotNull;

@RegistryKey(name = DecayScore.TYPE, type = DecayScore.class)
public class DecayScoreToElastic<C extends ElasticScoreFunctionTransformationContext<C>>
        extends AbstractScoreFunctionToElastic<DecayScore, C> {

    @Override
    protected FunctionScore.Builder.ContainerBuilder buildFunctionScore(
            final DecayScore scoreFunction,
            final Object props,
            final C context
    ) {
        return getBuilder(scoreFunction.getMode(), (switch (scoreFunction.getDecayScoreType()) {
            case DATE -> getDateDecayFunction(scoreFunction);
            case GEO -> getGeoDecayFunction(scoreFunction);
            case NUMERIC -> getNumericDecayFunction(scoreFunction);
        }).build()._toDecayFunction());
    }

    private static FunctionScore.Builder.ContainerBuilder getBuilder(final String mode, final DecayFunction decayFunction) {
        final var builder = new Builder();
        return switch (mode.toLowerCase(Locale.ROOT)) {
            case "linear" -> builder.linear(decayFunction);
            case "gauss" -> builder.gauss(decayFunction);
            case "exponential" -> builder.exp(decayFunction);
            default -> throw new IllegalArgumentException("Unsupported decay score mode: " + mode);
        };
    }

    private ObjectBuilder<DateDecayFunction> getDateDecayFunction(final DecayScore scoreFunction) {
        final var placementBuilder = new DecayPlacement.Builder<String, Time>();
        applyIfNotNull(scoreFunction.getDecay(), placementBuilder::decay);
        applyIfNotNull(scoreFunction.getOrigin(), String::valueOf, placementBuilder::origin);
        applyIfNotNull(scoreFunction.getScale(), MiscTransforms::toElasticTime, placementBuilder::scale);
        applyIfNotNull(scoreFunction.getOffset(), MiscTransforms::toElasticTime, placementBuilder::offset);

        return DecayFunctionBuilders.date()
                .field(scoreFunction.getField())
                .placement(placementBuilder.build());
    }

    private ObjectBuilder<GeoDecayFunction> getGeoDecayFunction(final DecayScore scoreFunction) {
        final var placementBuilder = new DecayPlacement.Builder<GeoLocation, String>();
        applyIfNotNull(scoreFunction.getDecay(), placementBuilder::decay);
        applyIfNotNull(scoreFunction.getOrigin(), MiscTransforms::toElasticOrigin, placementBuilder::origin);
        applyIfNotNull(scoreFunction.getScale(), String::valueOf, placementBuilder::scale);
        applyIfNotNull(scoreFunction.getOffset(), String::valueOf, placementBuilder::offset);

        return DecayFunctionBuilders.geo()
                .field(scoreFunction.getField())
                .placement(placementBuilder.build());
    }

    private ObjectBuilder<NumericDecayFunction> getNumericDecayFunction(final DecayScore scoreFunction) {
        final var placementBuilder = new DecayPlacement.Builder<Double, Double>();
        applyIfNotNull(scoreFunction.getDecay(), placementBuilder::decay);

        if (scoreFunction.getOrigin() instanceof final Number origin) {
            placementBuilder.origin(origin.doubleValue());
        }

        if (scoreFunction.getScale() instanceof final Number scale) {
            placementBuilder.scale(scale.doubleValue());
        }

        if (scoreFunction.getOffset() instanceof final Number offset) {
            placementBuilder.offset(offset.doubleValue());
        }

        return DecayFunctionBuilders.numeric()
                .field(scoreFunction.getField())
                .placement(placementBuilder.build());
    }
}
