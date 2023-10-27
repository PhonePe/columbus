package com.phonepe.platform.columbus.elastic.internal.search.aggregation.bucket;

import com.phonepe.platform.columbus.elastic.internal.search.aggregation.parser.bucket.TermsAggregationParser;
import com.phonepe.platform.columbus.elastic.search.aggregation.ElasticAggregationParserResolutionContext;
import com.phonepe.platform.columbus.registry.factory.RegistryKey;
import com.phonepe.platform.columbus.search.aggregation.AggregationParser;
import com.phonepe.platform.columbus.search.aggregator.bucket.TermsAggregator;
import org.elasticsearch.search.aggregations.Aggregation;

import java.util.Map;

@RegistryKey(name = TermsAggregator.TYPE, type = TermsAggregator.class)
public class TermsAggregationParserForElastic<C extends ElasticAggregationParserResolutionContext<C>>
        extends BucketAggregationParserForElastic<TermsAggregator, C> {

    protected AggregationParser<Aggregation> createParser(
            final TermsAggregator aggregator,
            final Map<String, AggregationParser<Aggregation>> subAggregationParsers,
            final C context
    ) {
        return TermsAggregationParser.builder()
                .subAggregationParsers(subAggregationParsers)
                .build();
    }
}
