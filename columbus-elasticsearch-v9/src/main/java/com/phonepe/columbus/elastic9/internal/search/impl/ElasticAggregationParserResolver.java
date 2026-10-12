package com.phonepe.columbus.elastic9.internal.search.impl;

import com.phonepe.columbus.elastic9.search.aggregation.ElasticAggregationParserResolutionContext;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;

@SuperBuilder(toBuilder = true)
public class ElasticAggregationParserResolver
        extends ElasticAggregationParserResolutionContext<ElasticAggregationParserResolver> {

    @Override
    protected @NonNull ElasticAggregationParserResolver self() {
        return this;
    }
}
