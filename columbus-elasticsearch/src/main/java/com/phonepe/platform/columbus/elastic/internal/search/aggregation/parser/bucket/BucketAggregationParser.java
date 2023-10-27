package com.phonepe.platform.columbus.elastic.internal.search.aggregation.parser.bucket;

import com.phonepe.platform.columbus.elastic.internal.search.aggregation.parser.ElasticAggregationParser;
import com.phonepe.platform.columbus.search.aggregation.AggregationParser;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Singular;
import lombok.experimental.SuperBuilder;
import org.elasticsearch.search.aggregations.Aggregation;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

@Getter(AccessLevel.PROTECTED)
@SuperBuilder(toBuilder = true)
public abstract class BucketAggregationParser implements ElasticAggregationParser {

    @Singular(ignoreNullCollections = true)
    private final Map<String, AggregationParser<Aggregation>> subAggregationParsers;

    protected Stream<com.phonepe.platform.columbus.search.aggregation.Aggregation> parseSubAggregations(
            final List<Aggregation> aggregations
    ) {
        return aggregations.stream()
                .map(aggregation -> subAggregationParsers.get(aggregation.getName()).parse(aggregation))
                .filter(Objects::nonNull);
    }
}
