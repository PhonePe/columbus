package com.phonepe.commons.columbus.elastic.internal.search.aggregation.parser.metrics;

import com.phonepe.commons.columbus.elastic.internal.search.aggregation.parser.ElasticAggregationParser;
import com.phonepe.commons.columbus.search.aggregation.metrics.CardinalityAggregation;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.metrics.ParsedCardinality;

public class CardinalityAggregationParser implements ElasticAggregationParser {

    @Override
    public CardinalityAggregation parse(final Aggregation result) {
        if (result instanceof ParsedCardinality cardinality) {
            return parseCardinality(cardinality);
        }
        throw new IllegalArgumentException("Expected ParsedCardinality, got '%s', with name '%s'".formatted(
                result.getClass().getSimpleName(), result.getName()));
    }

    private CardinalityAggregation parseCardinality(final ParsedCardinality cardinality) {
        return CardinalityAggregation.builder()
                .name(cardinality.getName())
                .count(cardinality.getValue())
                .build();
    }
}
