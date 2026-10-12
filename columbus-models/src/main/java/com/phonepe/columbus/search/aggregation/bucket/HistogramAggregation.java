package com.phonepe.columbus.search.aggregation.bucket;

import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.phonepe.columbus.search.aggregation.AbstractAggregation;
import com.phonepe.columbus.search.aggregation.Aggregation;
import com.phonepe.columbus.search.aggregator.bucket.HistogramAggregator;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.Singular;
import lombok.experimental.SuperBuilder;

import java.util.Map;

@JsonClassDescription("Result of a histogram aggregation containing multiple buckets representing value ranges")
@Data
@SuperBuilder(toBuilder = true)
public class HistogramAggregation extends AbstractAggregation {

    @JsonPropertyDescription("Map of bucket keys to their corresponding bucket results")
    @Singular(ignoreNullCollections = true)
    private final Map<@NonNull String, @NonNull Bucket> buckets;

    @Override
    public @NonNull String getType() {
        return HistogramAggregator.TYPE;
    }

    @JsonClassDescription("Individual bucket within a histogram aggregation representing a specific value range")
    @Data
    @Builder
    public static final class Bucket {

        @JsonPropertyDescription("The key representing the start of the bucket range")
        private final Number key;

        @JsonPropertyDescription("Number of documents that fall within this bucket range")
        private final long count;

        @JsonPropertyDescription("Map of sub-aggregation results computed within this bucket")
        @Singular(ignoreNullCollections = true)
        private final Map<@NonNull String, @NonNull Aggregation> aggregations;
    }
}
