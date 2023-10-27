package com.phonepe.platform.columbus.elastic.internal.search.impl;

import com.phonepe.platform.columbus.elastic.search.aggregator.ElasticAggregatorTransformationContext;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;

@SuperBuilder(toBuilder = true)
public class ElasticAggregatorTransformer extends ElasticAggregatorTransformationContext<ElasticAggregatorTransformer> {

    @Override
    protected @NonNull ElasticAggregatorTransformer self() {
        return this;
    }
}
