package com.phonepe.commons.columbus.search.aggregation.bucket;

import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.phonepe.commons.columbus.search.aggregation.AbstractAggregation;
import com.phonepe.commons.columbus.search.aggregation.Aggregation;
import com.phonepe.commons.columbus.search.aggregator.bucket.FilterAggregator;
import lombok.Data;
import lombok.NonNull;
import lombok.Singular;
import lombok.experimental.SuperBuilder;

import java.util.Map;

@JsonClassDescription("Result of a filter aggregation containing documents that matched the filter query")
@Data
@SuperBuilder(toBuilder = true)
public class FilterAggregation extends AbstractAggregation {

    @JsonPropertyDescription("Number of documents that matched the filter")
    private final long count;

    @JsonPropertyDescription("Map of sub-aggregation results computed within this filtered bucket")
    @Singular(ignoreNullCollections = true)
    private final Map<@NonNull String, @NonNull Aggregation> aggregations;

    @Override
    public @NonNull String getType() {
        return FilterAggregator.TYPE;
    }
}
