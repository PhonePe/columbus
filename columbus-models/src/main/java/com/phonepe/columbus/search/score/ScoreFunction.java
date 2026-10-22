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
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.NonNull;

/**
 * A score function defines how the scores are calculated for the matching documents.
 */
@JsonClassDescription("Interface for score functions that modify document relevance scores in function_score queries")
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(WeightScore.class),
        @JsonSubTypes.Type(FieldScore.class),
        @JsonSubTypes.Type(RandomScore.class),
        @JsonSubTypes.Type(ScriptScore.class),
        @JsonSubTypes.Type(DecayScore.class)
})
public interface ScoreFunction {

    @JsonPropertyDescription("The type identifier for this score function")
    @NonNull
    String getType();
}
