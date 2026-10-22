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
import com.phonepe.columbus.search.query.Query;
import lombok.Data;
import lombok.experimental.SuperBuilder;

@JsonClassDescription("Abstract base class for all score function implementations providing common weight and filter properties")
@Data
@SuperBuilder(toBuilder = true)
public abstract class AbstractScoreFunction implements ScoreFunction {

    @JsonPropertyDescription("Optional weight multiplier to apply to the score computed by this function")
    private Float weight;

    @JsonPropertyDescription("Optional filter query - this function only applies to documents matching this filter")
    private Query query;
}
