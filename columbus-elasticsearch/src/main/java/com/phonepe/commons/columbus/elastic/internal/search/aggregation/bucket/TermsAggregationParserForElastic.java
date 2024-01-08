package com.phonepe.commons.columbus.elastic.internal.search.aggregation.bucket;

import com.phonepe.commons.columbus.elastic.internal.search.aggregation.parser.bucket.TermsAggregationParser;
import com.phonepe.commons.columbus.elastic.search.aggregation.ElasticAggregationParserResolutionContext;
import com.phonepe.commons.columbus.registry.factory.RegistryKey;
import com.phonepe.commons.columbus.search.aggregation.AggregationParser;
import com.phonepe.commons.columbus.search.aggregator.bucket.TermsAggregator;
import org.elasticsearch.search.aggregations.Aggregation;

import java.util.Map;

@RegistryKey(name = TermsAggregator.TYPE, type = TermsAggregator.class)
public class TermsAggregationParserForElastic<C extends ElasticAggregationParserResolutionContext<C>>
        extends BucketAggregationParserForElastic<TermsAggregator, C> {

    protected AggregationParser<Aggregation> createParser(
            final TermsAggregator aggregator,
            final Map<String, AggregationParser<Aggregation>> subAggregationParsers,
            final Object props,
            final C context
    ) {
        return TermsAggregationParser.builder()
                .subAggregationParsers(subAggregationParsers)
                .build();
    }
}
