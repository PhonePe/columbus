package com.phonepe.columbus.elastic9.internal.search.aggregation.bucket;

import co.elastic.clients.elasticsearch._types.aggregations.Aggregate;
import com.phonepe.columbus.elastic9.internal.search.aggregation.parser.bucket.FiltersAggregationParser;
import com.phonepe.columbus.elastic9.search.aggregation.ElasticAggregationParserResolutionContext;
import com.phonepe.columbus.registry.factory.RegistryKey;
import com.phonepe.columbus.search.aggregation.AggregationParser;
import com.phonepe.columbus.search.aggregator.bucket.FiltersAggregator;

import java.util.Map;

@RegistryKey(name = FiltersAggregator.TYPE, type = FiltersAggregator.class)
public class FiltersAggregationParserForElastic<C extends ElasticAggregationParserResolutionContext<C>>
        extends BucketAggregationParserForElastic<FiltersAggregator, C> {

    protected AggregationParser<Aggregate> createParser(
            final FiltersAggregator aggregator,
            final Map<String, AggregationParser<Aggregate>> subAggregationParsers,
            final Object props,
            final C context
    ) {
        return FiltersAggregationParser.builder()
                .subAggregationParsers(subAggregationParsers)
                .build();
    }
}
