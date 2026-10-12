package com.phonepe.columbus.search.aggregation.metrics;

import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.phonepe.columbus.search.aggregation.AbstractAggregation;
import com.phonepe.columbus.search.aggregator.metrics.StatsAggregator;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;

@JsonClassDescription("Result of a stats aggregation containing statistical metrics computed over numeric field values")
@Data
@SuperBuilder(toBuilder = true)
public class StatsAggregation extends AbstractAggregation {

    @JsonPropertyDescription("Number of documents included in the statistics")
    private final long count;

    @JsonPropertyDescription("Minimum value found in the field")
    private final double minimum;

    @JsonPropertyDescription("Maximum value found in the field")
    private final double maximum;

    @JsonPropertyDescription("Sum of all values in the field")
    private final double sum;

    @JsonPropertyDescription("Average (mean) of all values in the field")
    private final double average;

    @Override
    public @NonNull String getType() {
        return StatsAggregator.TYPE;
    }
}
