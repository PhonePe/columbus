package com.phonepe.columbus.elastic9.internal.search.aggregation.bucket;

import co.elastic.clients.elasticsearch._types.aggregations.Aggregate;
import com.phonepe.columbus.elastic9.internal.search.aggregation.parser.bucket.RangeAggregationParser;
import com.phonepe.columbus.elastic9.search.aggregation.ElasticAggregationParserResolutionContext;
import com.phonepe.columbus.registry.factory.RegistryKey;
import com.phonepe.columbus.search.aggregation.AggregationParser;
import com.phonepe.columbus.search.aggregator.bucket.RangeAggregator;

import java.util.Map;

@RegistryKey(name = RangeAggregator.TYPE, type = RangeAggregator.class)
public class RangeAggregationParserForElastic<C extends ElasticAggregationParserResolutionContext<C>>
        extends BucketAggregationParserForElastic<RangeAggregator, C> {

    protected AggregationParser<Aggregate> createParser(
            final RangeAggregator aggregator,
            final Map<String, AggregationParser<Aggregate>> subAggregationParsers,
            final Object props,
            final C context
    ) {
        return RangeAggregationParser.builder()
                .subAggregationParsers(subAggregationParsers)
                .build();
    }
}
