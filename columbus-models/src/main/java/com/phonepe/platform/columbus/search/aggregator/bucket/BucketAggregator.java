package com.phonepe.platform.columbus.search.aggregator.bucket;

import com.phonepe.platform.columbus.search.aggregator.Aggregator;
import com.phonepe.platform.columbus.search.aggregator.BaseAggregator;
import lombok.Data;
import lombok.NonNull;
import lombok.Singular;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@SuperBuilder(toBuilder = true)
public abstract class BucketAggregator extends BaseAggregator {

    @Singular(ignoreNullCollections = true)
    private List<@NonNull Aggregator> subAggregations;
}
