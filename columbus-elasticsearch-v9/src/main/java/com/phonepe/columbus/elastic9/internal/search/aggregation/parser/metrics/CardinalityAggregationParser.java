package com.phonepe.columbus.elastic9.internal.search.aggregation.parser.metrics;

import co.elastic.clients.elasticsearch._types.aggregations.Aggregate;
import co.elastic.clients.elasticsearch._types.aggregations.CardinalityAggregate;
import com.phonepe.columbus.elastic9.internal.search.aggregation.parser.ElasticAggregationParser;
import com.phonepe.columbus.search.aggregation.metrics.CardinalityAggregation;

public class CardinalityAggregationParser implements ElasticAggregationParser {

    @Override
    public CardinalityAggregation parse(final Aggregate result, final String name) {
        if (result.isCardinality()) {
            return parseCardinality(result.cardinality(), name);
        }
        throw new IllegalArgumentException("Expected CardinalityAggregate, got '%s', with name '%s'".formatted(
                result.getClass().getSimpleName(), name));
    }

    private CardinalityAggregation parseCardinality(final CardinalityAggregate cardinality, final String name) {
        return CardinalityAggregation.builder()
                .name(name)
                .count(cardinality.value())
                .build();
    }
}
