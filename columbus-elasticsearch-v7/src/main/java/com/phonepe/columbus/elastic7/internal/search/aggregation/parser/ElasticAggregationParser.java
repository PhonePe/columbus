package com.phonepe.columbus.elastic7.internal.search.aggregation.parser;

import com.phonepe.columbus.search.aggregation.AggregationParser;
import org.elasticsearch.search.aggregations.Aggregation;

/**
 * An {@link AggregationParser} to parse ElasticSearch's {@link Aggregation}s.
 */
public interface ElasticAggregationParser extends AggregationParser<Aggregation> {

    default com.phonepe.columbus.search.aggregation.Aggregation parse(
            final Aggregation result,
            final String name
    ) {
        // Delegating to parse(result) as name is already part of result in ES 7 Aggregation
        return parse(result);
    }
}
