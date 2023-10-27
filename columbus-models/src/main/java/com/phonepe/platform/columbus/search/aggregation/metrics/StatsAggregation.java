package com.phonepe.platform.columbus.search.aggregation.metrics;

import com.phonepe.platform.columbus.search.aggregation.BaseAggregation;
import com.phonepe.platform.columbus.search.aggregator.metrics.StatsAggregator;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder(toBuilder = true)
public class StatsAggregation extends BaseAggregation {

    private final long count;
    private final double minimum;
    private final double maximum;
    private final double sum;
    private final double average;

    @Override
    public @NonNull String getType() {
        return StatsAggregator.TYPE;
    }
}
