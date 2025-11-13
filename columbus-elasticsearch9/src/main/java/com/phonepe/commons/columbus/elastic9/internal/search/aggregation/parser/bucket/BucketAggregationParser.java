package com.phonepe.commons.columbus.elastic9.internal.search.aggregation.parser.bucket;

import co.elastic.clients.elasticsearch._types.aggregations.Aggregate;
import com.phonepe.commons.columbus.elastic9.internal.search.aggregation.parser.ElasticAggregationParser;
import com.phonepe.commons.columbus.search.aggregation.Aggregation;
import com.phonepe.commons.columbus.search.aggregation.AggregationParser;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Singular;
import lombok.experimental.SuperBuilder;

import java.util.Map;
import java.util.stream.Stream;

@Getter(AccessLevel.PROTECTED)
@SuperBuilder(toBuilder = true)
public abstract class BucketAggregationParser implements ElasticAggregationParser {

    @Singular(ignoreNullCollections = true)
    private final Map<String, AggregationParser<Aggregate>> subAggregationParsers;

    protected Stream<Aggregation> parseSubAggregations(
            final Map<String, Aggregate> aggregates
    ) {
        return aggregates.entrySet().stream().map(entry ->
                subAggregationParsers.get(entry.getKey()).parse(entry.getValue(), entry.getKey()));
    }
}
