package com.phonepe.commons.columbus.search.aggregation.metrics;

import com.phonepe.commons.columbus.search.aggregation.AbstractAggregation;
import com.phonepe.commons.columbus.search.aggregator.metrics.TopHitsAggregator;
import com.phonepe.commons.columbus.search.misc.result.Result;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder(toBuilder = true)
public class TopHitsAggregation<T> extends AbstractAggregation {

    @NonNull
    private final Result<T> topHits;

    @Override
    public @NonNull String getType() {
        return TopHitsAggregator.TYPE;
    }
}
