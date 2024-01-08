package com.phonepe.commons.columbus.elastic.internal.search.aggregation.parser;

import com.phonepe.commons.columbus.search.aggregation.AggregationParser;
import org.elasticsearch.search.aggregations.Aggregation;

/**
 * An {@link AggregationParser} to parse ElasticSearch's {@link Aggregation}s.
 */
public interface ElasticAggregationParser extends AggregationParser<Aggregation> {
}
