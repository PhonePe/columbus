package com.phonepe.platform.columbus.elastic.internal.search.aggregation.bucket;

import com.phonepe.platform.columbus.elastic.internal.search.aggregation.parser.bucket.RangeAggregationParser;
import com.phonepe.platform.columbus.elastic.search.aggregation.ElasticAggregationParserResolutionContext;
import com.phonepe.platform.columbus.registry.factory.RegistryKey;
import com.phonepe.platform.columbus.search.aggregation.AggregationParser;
import com.phonepe.platform.columbus.search.aggregator.bucket.RangeAggregator;
import org.elasticsearch.search.aggregations.Aggregation;

import java.util.Map;

@RegistryKey(name = RangeAggregator.TYPE, type = RangeAggregator.class)
public class RangeAggregationParserForElastic<C extends ElasticAggregationParserResolutionContext<C>>
        extends BucketAggregationParserForElastic<RangeAggregator, C> {

    protected AggregationParser<Aggregation> createParser(
            final RangeAggregator aggregator,
            final Map<String, AggregationParser<Aggregation>> subAggregationParsers,
            final C context
    ) {
        return RangeAggregationParser.builder()
                .subAggregationParsers(subAggregationParsers)
                .build();
    }
}
