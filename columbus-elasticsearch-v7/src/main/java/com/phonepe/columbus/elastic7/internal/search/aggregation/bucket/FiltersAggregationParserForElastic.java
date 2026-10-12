package com.phonepe.columbus.elastic7.internal.search.aggregation.bucket;

import com.phonepe.columbus.elastic7.internal.search.aggregation.parser.bucket.FiltersAggregationParser;
import com.phonepe.columbus.elastic7.search.aggregation.ElasticAggregationParserResolutionContext;
import com.phonepe.columbus.registry.factory.RegistryKey;
import com.phonepe.columbus.search.aggregation.AggregationParser;
import com.phonepe.columbus.search.aggregator.bucket.FiltersAggregator;
import org.elasticsearch.search.aggregations.Aggregation;

import java.util.Map;

@RegistryKey(name = FiltersAggregator.TYPE, type = FiltersAggregator.class)
public class FiltersAggregationParserForElastic<C extends ElasticAggregationParserResolutionContext<C>>
        extends BucketAggregationParserForElastic<FiltersAggregator, C> {

    protected AggregationParser<Aggregation> createParser(
            final FiltersAggregator aggregator,
            final Map<String, AggregationParser<Aggregation>> subAggregationParsers,
            final Object props,
            final C context
    ) {
        return FiltersAggregationParser.builder()
                .subAggregationParsers(subAggregationParsers)
                .build();
    }
}
