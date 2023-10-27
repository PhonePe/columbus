package com.phonepe.platform.columbus.search.aggregator.metrics;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.phonepe.platform.columbus.search.aggregator.BaseAggregator;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@Data
@Jacksonized
@SuperBuilder(toBuilder = true)
@JsonTypeName(StatsAggregator.TYPE)
public class StatsAggregator extends BaseAggregator {

    public static final String TYPE = "STATS";

    private String field;
    private Object missingValue;

    @Override
    public @NonNull String getType() {
        return TYPE;
    }
}
