package com.phonepe.commons.columbus.search.aggregation.metrics;

import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.phonepe.commons.columbus.search.aggregation.AbstractAggregation;
import com.phonepe.commons.columbus.search.aggregator.metrics.CardinalityAggregator;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;

@JsonClassDescription("Result of a cardinality aggregation containing the approximate count of distinct values")
@Data
@SuperBuilder(toBuilder = true)
public class CardinalityAggregation extends AbstractAggregation {

    @JsonPropertyDescription("Approximate count of distinct values in the field")
    private final long count;

    @Override
    public @NonNull String getType() {
        return CardinalityAggregator.TYPE;
    }
}
