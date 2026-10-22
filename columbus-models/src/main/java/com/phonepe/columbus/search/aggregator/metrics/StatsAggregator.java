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
package com.phonepe.columbus.search.aggregator.metrics;

import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.phonepe.columbus.search.aggregator.AbstractAggregator;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@JsonClassDescription("Metrics aggregator that computes statistical metrics (min, max, sum, count, avg) over numeric field values")
@Data
@Jacksonized
@SuperBuilder(toBuilder = true)
@JsonTypeName(StatsAggregator.TYPE)
public class StatsAggregator extends AbstractAggregator {

    public static final String TYPE = "STATS";

    @JsonPropertyDescription("The numeric field to calculate statistics for")
    private String field;

    @JsonPropertyDescription("Default value to use for documents where the field is missing")
    private Object missingValue;

    @Override
    public @NonNull String getType() {
        return TYPE;
    }
}
