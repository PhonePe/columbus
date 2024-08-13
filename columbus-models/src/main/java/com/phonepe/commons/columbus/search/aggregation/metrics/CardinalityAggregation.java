package com.phonepe.commons.columbus.search.aggregation.metrics;

import com.phonepe.commons.columbus.search.aggregation.AbstractAggregation;
import com.phonepe.commons.columbus.search.aggregator.metrics.CardinalityAggregator;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder(toBuilder = true)
public class CardinalityAggregation extends AbstractAggregation {

    private final long count;

    @Override
    public @NonNull String getType() {
        return CardinalityAggregator.TYPE;
    }
}
