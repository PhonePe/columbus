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
package com.phonepe.columbus.search.aggregation.bucket;

import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.phonepe.columbus.search.aggregation.AbstractAggregation;
import com.phonepe.columbus.search.aggregation.Aggregation;
import com.phonepe.columbus.search.aggregator.bucket.FiltersAggregator;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.Singular;
import lombok.experimental.SuperBuilder;

import java.util.Map;

@JsonClassDescription("Result of a filters aggregation containing multiple named buckets, each with documents matching its filter")
@Data
@SuperBuilder(toBuilder = true)
public class FiltersAggregation extends AbstractAggregation {

    @JsonPropertyDescription("Map of bucket names to their corresponding bucket results")
    @Singular(ignoreNullCollections = true)
    private final Map<@NonNull String, @NonNull Bucket> buckets;

    @Override
    public @NonNull String getType() {
        return FiltersAggregator.TYPE;
    }

    @JsonClassDescription("Bucket result for a single filter containing matching documents and sub-aggregations")
    @Data
    @Builder
    public static final class Bucket {

        @JsonPropertyDescription("Number of documents that matched this bucket's filter")
        private final long count;

        @JsonPropertyDescription("Map of sub-aggregation results computed within this bucket")
        @Singular(ignoreNullCollections = true)
        private final Map<@NonNull String, @NonNull Aggregation> aggregations;
    }
}
