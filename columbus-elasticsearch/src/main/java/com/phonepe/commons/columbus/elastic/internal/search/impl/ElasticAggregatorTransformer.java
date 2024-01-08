package com.phonepe.commons.columbus.elastic.internal.search.impl;

import com.phonepe.commons.columbus.elastic.search.aggregator.ElasticAggregatorTransformationContext;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;

@SuperBuilder(toBuilder = true)
public class ElasticAggregatorTransformer extends ElasticAggregatorTransformationContext<ElasticAggregatorTransformer> {

    @Override
    protected @NonNull ElasticAggregatorTransformer self() {
        return this;
    }
}
