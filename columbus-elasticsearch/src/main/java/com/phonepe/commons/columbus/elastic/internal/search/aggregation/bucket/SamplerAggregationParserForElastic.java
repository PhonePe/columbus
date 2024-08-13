package com.phonepe.commons.columbus.elastic.internal.search.aggregation.bucket;

import com.phonepe.commons.columbus.elastic.internal.search.aggregation.parser.bucket.SamplerAggregationParser;
import com.phonepe.commons.columbus.elastic.search.aggregation.ElasticAggregationParserResolutionContext;
import com.phonepe.commons.columbus.registry.factory.RegistryKey;
import com.phonepe.commons.columbus.search.aggregation.AggregationParser;
import com.phonepe.commons.columbus.search.aggregator.bucket.SamplerAggregator;
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
