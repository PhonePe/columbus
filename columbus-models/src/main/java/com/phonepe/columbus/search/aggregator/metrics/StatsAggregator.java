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
