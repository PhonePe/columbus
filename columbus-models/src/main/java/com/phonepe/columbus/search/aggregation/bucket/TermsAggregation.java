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
import com.phonepe.columbus.search.aggregator.bucket.TermsAggregator;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.Singular;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.Map;

@JsonClassDescription("Result of a terms aggregation containing buckets for each unique term value in the field")
@Data
@SuperBuilder(toBuilder = true)
public class TermsAggregation extends AbstractAggregation {

    @JsonPropertyDescription("List of term buckets sorted by the specified order criteria")
    @Singular(ignoreNullCollections = true)
    private final List<@NonNull Bucket> buckets;

    @JsonPropertyDescription("Upper bound of the error in document counts across all buckets")
    private final long countError;

    @JsonPropertyDescription("Number of documents that didn't make it into the top buckets")
    private final long otherDocCount;

    @Override
    public @NonNull String getType() {
        return TermsAggregator.TYPE;
    }

    @JsonClassDescription("Bucket result for a single term value containing matching documents and sub-aggregations")
    @Data
    @Builder
    public static final class Bucket {

        @JsonPropertyDescription("The term value that defines this bucket")
        private final Object key;

        @JsonPropertyDescription("Number of documents containing this term")
        private final long count;

        @JsonPropertyDescription("Upper bound error for the document count in this bucket")
        private final long countError;

        @JsonPropertyDescription("Map of sub-aggregation results computed within this bucket")
        @Singular(ignoreNullCollections = true)
        private final Map<@NonNull String, @NonNull Aggregation> aggregations;
    }
}
