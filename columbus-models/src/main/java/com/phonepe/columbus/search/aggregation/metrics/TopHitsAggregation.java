package com.phonepe.columbus.search.aggregation.metrics;

import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.phonepe.columbus.search.aggregation.AbstractAggregation;
import com.phonepe.columbus.search.aggregator.metrics.TopHitsAggregator;
import com.phonepe.columbus.search.misc.result.Result;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;

@JsonClassDescription("Result of a top hits aggregation containing the top matching documents within each bucket")
@Data
@SuperBuilder(toBuilder = true)
public class TopHitsAggregation<T> extends AbstractAggregation {

    @NonNull
    @JsonPropertyDescription("Search result containing the top matching documents with their scores and metadata")
    private final Result<T> topHits;

    @Override
    public @NonNull String getType() {
        return TopHitsAggregator.TYPE;
    }
}
