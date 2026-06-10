package com.phonepe.commons.columbus.search.aggregator.metrics;

import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.phonepe.commons.columbus.search.aggregator.AbstractAggregator;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@JsonClassDescription("Metrics aggregator that calculates the approximate count of distinct values in a field")
@Data
@Jacksonized
@SuperBuilder(toBuilder = true)
@JsonTypeName(CardinalityAggregator.TYPE)
public class CardinalityAggregator extends AbstractAggregator {

    public static final String TYPE = "CARDINALITY";

    @JsonPropertyDescription("The field to calculate distinct value count for")
    private String field;

    @JsonPropertyDescription("Default value to use for documents where the field is missing")
    private Object missingValue;

    @JsonPropertyDescription("Precision threshold for the HyperLogLog++ algorithm (higher = more accurate but more memory)")
    private Integer precision;

    @Override
    public @NonNull String getType() {
        return TYPE;
    }
}
