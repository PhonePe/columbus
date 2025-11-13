package com.phonepe.commons.columbus.elastic9.internal.search.aggregation.parser;

import co.elastic.clients.elasticsearch._types.aggregations.Aggregate;
import com.phonepe.commons.columbus.search.aggregation.Aggregation;
import com.phonepe.commons.columbus.search.aggregation.AggregationParser;

/**
 * An {@link AggregationParser} to parse ElasticSearch's {@link Aggregate}s.
 */
public interface ElasticAggregationParser extends AggregationParser<Aggregate> {

    default Aggregation parse(final Aggregate result) {
        throw new IllegalArgumentException("ES9 aggregation parser requires aggregation name to parse");
    }
}
