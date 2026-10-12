package com.phonepe.columbus.elastic7.internal.search.impl;

import com.phonepe.columbus.elastic7.search.aggregator.ElasticAggregatorTransformationContext;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;

@SuperBuilder(toBuilder = true)
public class ElasticAggregatorTransformer extends ElasticAggregatorTransformationContext<ElasticAggregatorTransformer> {

    @Override
    protected @NonNull ElasticAggregatorTransformer self() {
        return this;
    }
}
