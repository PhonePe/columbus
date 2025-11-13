package com.phonepe.commons.columbus.elastic9.internal.search.aggregation.bucket;

import co.elastic.clients.elasticsearch._types.aggregations.Aggregate;
import com.phonepe.commons.columbus.elastic9.internal.search.aggregation.parser.bucket.HistogramAggregationParser;
import com.phonepe.commons.columbus.elastic9.search.aggregation.ElasticAggregationParserResolutionContext;
import com.phonepe.commons.columbus.registry.factory.RegistryKey;
import com.phonepe.commons.columbus.search.aggregation.AggregationParser;
import com.phonepe.commons.columbus.search.aggregator.bucket.HistogramAggregator;

import java.util.Map;

@RegistryKey(name = HistogramAggregator.TYPE, type = HistogramAggregator.class)
public class HistogramAggregationParserForElastic<C extends ElasticAggregationParserResolutionContext<C>>
        extends BucketAggregationParserForElastic<HistogramAggregator, C> {

    protected AggregationParser<Aggregate> createParser(
            final HistogramAggregator aggregator,
            final Map<String, AggregationParser<Aggregate>> subAggregationParsers,
            final Object props,
            final C context
    ) {
        return HistogramAggregationParser.builder()
                .subAggregationParsers(subAggregationParsers)
                .build();
    }
}
