package com.phonepe.commons.columbus.elastic.internal.search.impl;

import com.phonepe.commons.columbus.elastic.search.aggregation.ElasticAggregationParserResolutionContext;
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
