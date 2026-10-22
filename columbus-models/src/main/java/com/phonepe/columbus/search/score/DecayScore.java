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
package com.phonepe.columbus.search.score;

import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@JsonClassDescription("Score function that reduces score based on distance from an origin point using decay functions (linear, exp, gauss)")
@Data
@Jacksonized
@SuperBuilder(toBuilder = true)
@JsonTypeName(DecayScore.TYPE)
public class DecayScore extends AbstractScoreFunction {

    public static final String TYPE = "DECAY";

    @JsonPropertyDescription("Decay function shape to use (linear, exp, gauss)")
    private String mode;

    @JsonPropertyDescription("The numeric, date, or geo-point field to calculate distance from origin")
    private String field;

    @JsonPropertyDescription("The origin point from which to calculate distance")
    private Object origin;

    @JsonPropertyDescription("Distance from origin at which scores should be computed at the decay rate")
    private Object scale;

    @JsonPropertyDescription("Optional offset to add to distance before applying decay")
    private Object offset;

    @JsonPropertyDescription("Score at the scale distance (default: 0.5, meaning score is halved at scale distance)")
    private Double decay;

    @Builder.Default
    private DecayScoreType decayScoreType = DecayScoreType.NUMERIC;

    public enum DecayScoreType {
        NUMERIC,
        GEO,
        DATE
    }

    @Override
    public @NonNull String getType() {
        return TYPE;
    }
}
