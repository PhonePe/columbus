package com.phonepe.platform.columbus.elastic.internal.search.impl;

import com.phonepe.platform.columbus.elastic.search.aggregation.ElasticAggregationParserResolutionContext;
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
