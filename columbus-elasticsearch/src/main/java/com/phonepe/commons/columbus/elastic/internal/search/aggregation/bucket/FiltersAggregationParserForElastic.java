package com.phonepe.commons.columbus.elastic.internal.search.aggregation.bucket;

import com.phonepe.commons.columbus.elastic.internal.search.aggregation.parser.bucket.FiltersAggregationParser;
import com.phonepe.commons.columbus.elastic.search.aggregation.ElasticAggregationParserResolutionContext;
import com.phonepe.commons.columbus.registry.factory.RegistryKey;
import com.phonepe.commons.columbus.search.aggregation.AggregationParser;
import com.phonepe.commons.columbus.search.aggregator.bucket.FiltersAggregator;
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
