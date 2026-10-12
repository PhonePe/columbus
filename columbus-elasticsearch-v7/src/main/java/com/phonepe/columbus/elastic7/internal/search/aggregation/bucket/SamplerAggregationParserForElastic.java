package com.phonepe.columbus.elastic7.internal.search.aggregation.bucket;

import com.phonepe.columbus.elastic7.internal.search.aggregation.parser.bucket.SamplerAggregationParser;
import com.phonepe.columbus.elastic7.search.aggregation.ElasticAggregationParserResolutionContext;
import com.phonepe.columbus.registry.factory.RegistryKey;
import com.phonepe.columbus.search.aggregation.AggregationParser;
import com.phonepe.columbus.search.aggregator.bucket.SamplerAggregator;
import org.elasticsearch.search.aggregations.Aggregation;

import java.util.Map;

@RegistryKey(name = SamplerAggregator.TYPE, type = SamplerAggregator.class)
public class SamplerAggregationParserForElastic<C extends ElasticAggregationParserResolutionContext<C>>
        extends BucketAggregationParserForElastic<SamplerAggregator, C> {

    protected AggregationParser<Aggregation> createParser(
            final SamplerAggregator aggregator,
            final Map<String, AggregationParser<Aggregation>> subAggregationParsers,
            final Object props,
            final C context
    ) {
        return SamplerAggregationParser.builder()
                .subAggregationParsers(subAggregationParsers)
                .build();
    }
}
