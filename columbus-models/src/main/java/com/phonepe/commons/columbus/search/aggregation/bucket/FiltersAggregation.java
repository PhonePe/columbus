package com.phonepe.commons.columbus.search.aggregation.bucket;

import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.phonepe.commons.columbus.search.aggregation.AbstractAggregation;
import com.phonepe.commons.columbus.search.aggregation.Aggregation;
import com.phonepe.commons.columbus.search.aggregator.bucket.FiltersAggregator;
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
