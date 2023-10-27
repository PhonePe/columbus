package com.phonepe.platform.columbus.search.aggregation.metrics;

import com.phonepe.platform.columbus.search.aggregation.BaseAggregation;
import com.phonepe.platform.columbus.search.aggregator.metrics.TopHitsAggregator;
import com.phonepe.platform.columbus.search.misc.result.Result;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder(toBuilder = true)
public class TopHitsAggregation<T> extends BaseAggregation {

    @NonNull
    private final Result<T> topHits;

    @Override
    public @NonNull String getType() {
        return TopHitsAggregator.TYPE;
    }
}
