package com.phonepe.columbus.elastic9.internal.search.aggregation.bucket;

import co.elastic.clients.elasticsearch._types.aggregations.Aggregate;
import com.phonepe.columbus.elastic9.internal.search.aggregation.parser.bucket.TermsAggregationParser;
import com.phonepe.columbus.elastic9.search.aggregation.ElasticAggregationParserResolutionContext;
import com.phonepe.columbus.registry.factory.RegistryKey;
import com.phonepe.columbus.search.aggregation.AggregationParser;
import com.phonepe.columbus.search.aggregator.bucket.TermsAggregator;

import java.util.Map;

@RegistryKey(name = TermsAggregator.TYPE, type = TermsAggregator.class)
public class TermsAggregationParserForElastic<C extends ElasticAggregationParserResolutionContext<C>>
        extends BucketAggregationParserForElastic<TermsAggregator, C> {

    protected AggregationParser<Aggregate> createParser(
            final TermsAggregator aggregator,
            final Map<String, AggregationParser<Aggregate>> subAggregationParsers,
            final Object props,
            final C context
    ) {
        return TermsAggregationParser.builder()
                .subAggregationParsers(subAggregationParsers)
                .build();
    }
}
