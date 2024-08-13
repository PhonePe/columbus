package com.phonepe.commons.columbus.search.aggregation.bucket;

import com.phonepe.commons.columbus.search.aggregation.AbstractAggregation;
import com.phonepe.commons.columbus.search.aggregation.Aggregation;
import com.phonepe.commons.columbus.search.aggregator.bucket.SamplerAggregator;
import lombok.Data;
import lombok.NonNull;
import lombok.Singular;
import lombok.experimental.SuperBuilder;

import java.util.Map;

@Data
@SuperBuilder(toBuilder = true)
public class SamplerAggregation extends AbstractAggregation {

    private final long count;

    @Singular(ignoreNullCollections = true)
    private final Map<@NonNull String, @NonNull Aggregation> aggregations;

    @Override
    public @NonNull String getType() {
        return SamplerAggregator.TYPE;
    }
}
