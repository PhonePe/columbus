package com.phonepe.commons.columbus.elastic9.internal.search.aggregation.bucket;

import co.elastic.clients.elasticsearch._types.aggregations.Aggregate;
import com.phonepe.commons.columbus.elastic9.internal.search.aggregation.parser.bucket.FilterAggregationParser;
import com.phonepe.commons.columbus.elastic9.search.aggregation.ElasticAggregationParserResolutionContext;
import com.phonepe.commons.columbus.registry.factory.RegistryKey;
import com.phonepe.commons.columbus.search.aggregation.AggregationParser;
import com.phonepe.commons.columbus.search.aggregator.bucket.FilterAggregator;

import java.util.Map;

@RegistryKey(name = FilterAggregator.TYPE, type = FilterAggregator.class)
public class FilterAggregationParserForElastic<C extends ElasticAggregationParserResolutionContext<C>>
        extends BucketAggregationParserForElastic<FilterAggregator, C> {

    protected AggregationParser<Aggregate> createParser(
            final FilterAggregator aggregator,
            final Map<String, AggregationParser<Aggregate>> subAggregationParsers,
            final Object props,
            final C context
    ) {
        return FilterAggregationParser.builder()
                .subAggregationParsers(subAggregationParsers)
                .build();
    }
}
