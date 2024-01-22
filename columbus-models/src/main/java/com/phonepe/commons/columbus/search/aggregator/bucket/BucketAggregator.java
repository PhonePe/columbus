package com.phonepe.commons.columbus.search.aggregator.bucket;

import com.phonepe.commons.columbus.search.aggregator.AbstractAggregator;
import com.phonepe.commons.columbus.search.aggregator.Aggregator;
import lombok.Data;
import lombok.NonNull;
import lombok.Singular;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@SuperBuilder(toBuilder = true)
public abstract class BucketAggregator extends AbstractAggregator {

    @Singular(ignoreNullCollections = true)
    private List<@NonNull Aggregator> subAggregators;
}
