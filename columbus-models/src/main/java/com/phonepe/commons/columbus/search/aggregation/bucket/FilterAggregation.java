package com.phonepe.commons.columbus.search.aggregation.bucket;

import com.phonepe.commons.columbus.search.aggregation.Aggregation;
import com.phonepe.commons.columbus.search.aggregation.BaseAggregation;
import com.phonepe.commons.columbus.search.aggregator.bucket.FilterAggregator;
import lombok.Data;
import lombok.NonNull;
import lombok.Singular;
import lombok.experimental.SuperBuilder;

import java.util.Map;

@Data
@SuperBuilder(toBuilder = true)
public class FilterAggregation extends BaseAggregation {

    private final long count;

    @Singular(ignoreNullCollections = true)
    private final Map<@NonNull String, @NonNull Aggregation> aggregations;

    @Override
    public @NonNull String getType() {
        return FilterAggregator.TYPE;
    }
}
