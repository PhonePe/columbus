package com.phonepe.columbus.elastic7.internal.search.aggregation.bucket;

import com.phonepe.columbus.elastic7.internal.search.aggregation.parser.bucket.HistogramAggregationParser;
import com.phonepe.columbus.elastic7.search.aggregation.ElasticAggregationParserResolutionContext;
import com.phonepe.columbus.registry.factory.RegistryKey;
import com.phonepe.columbus.search.aggregation.AggregationParser;
import com.phonepe.columbus.search.aggregator.bucket.HistogramAggregator;
import org.elasticsearch.search.aggregations.Aggregation;

import java.util.Map;

@RegistryKey(name = HistogramAggregator.TYPE, type = HistogramAggregator.class)
public class HistogramAggregationParserForElastic<C extends ElasticAggregationParserResolutionContext<C>>
        extends BucketAggregationParserForElastic<HistogramAggregator, C> {

    protected AggregationParser<Aggregation> createParser(
            final HistogramAggregator aggregator,
            final Map<String, AggregationParser<Aggregation>> subAggregationParsers,
            final Object props,
            final C context
    ) {
        return HistogramAggregationParser.builder()
                .subAggregationParsers(subAggregationParsers)
                .build();
    }
}
