package com.phonepe.columbus.elastic7.internal.search.aggregation.bucket;

import com.phonepe.columbus.elastic7.internal.search.aggregation.parser.bucket.FilterAggregationParser;
import com.phonepe.columbus.elastic7.search.aggregation.ElasticAggregationParserResolutionContext;
import com.phonepe.columbus.registry.factory.RegistryKey;
import com.phonepe.columbus.search.aggregation.AggregationParser;
import com.phonepe.columbus.search.aggregator.bucket.FilterAggregator;
import org.elasticsearch.search.aggregations.Aggregation;

import java.util.Map;

@RegistryKey(name = FilterAggregator.TYPE, type = FilterAggregator.class)
public class FilterAggregationParserForElastic<C extends ElasticAggregationParserResolutionContext<C>>
        extends BucketAggregationParserForElastic<FilterAggregator, C> {

    protected AggregationParser<Aggregation> createParser(
            final FilterAggregator aggregator,
            final Map<String, AggregationParser<Aggregation>> subAggregationParsers,
            final Object props,
            final C context
    ) {
        return FilterAggregationParser.builder()
                .subAggregationParsers(subAggregationParsers)
                .build();
    }
}
