package com.phonepe.commons.columbus.search.aggregator.metrics;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.phonepe.commons.columbus.search.aggregator.AbstractAggregator;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@Data
@Jacksonized
@SuperBuilder(toBuilder = true)
@JsonTypeName(CardinalityAggregator.TYPE)
public class CardinalityAggregator extends AbstractAggregator {

    public static final String TYPE = "CARDINALITY";

    private String field;
    private Object missingValue;
    private Integer precision;

    @Override
    public @NonNull String getType() {
        return TYPE;
    }
}
