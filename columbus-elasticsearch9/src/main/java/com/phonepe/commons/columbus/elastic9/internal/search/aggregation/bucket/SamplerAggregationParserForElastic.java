package com.phonepe.commons.columbus.elastic9.internal.search.aggregation.bucket;

import co.elastic.clients.elasticsearch._types.aggregations.Aggregate;
import com.phonepe.commons.columbus.elastic9.internal.search.aggregation.parser.bucket.SamplerAggregationParser;
import com.phonepe.commons.columbus.elastic9.search.aggregation.ElasticAggregationParserResolutionContext;
import com.phonepe.commons.columbus.registry.factory.RegistryKey;
import com.phonepe.commons.columbus.search.aggregation.AggregationParser;
import com.phonepe.commons.columbus.search.aggregator.bucket.SamplerAggregator;

import java.util.Map;

@RegistryKey(name = SamplerAggregator.TYPE, type = SamplerAggregator.class)
public class SamplerAggregationParserForElastic<C extends ElasticAggregationParserResolutionContext<C>>
        extends BucketAggregationParserForElastic<SamplerAggregator, C> {

    protected AggregationParser<Aggregate> createParser(
            final SamplerAggregator aggregator,
            final Map<String, AggregationParser<Aggregate>> subAggregationParsers,
            final Object props,
            final C context
    ) {
        return SamplerAggregationParser.builder()
                .subAggregationParsers(subAggregationParsers)
                .build();
    }
}
