package com.phonepe.platform.columbus.elastic.internal.search.aggregation.parser.bucket;

import com.phonepe.platform.columbus.search.aggregation.bucket.RangeAggregation;
import com.phonepe.platform.columbus.search.aggregation.bucket.RangeAggregation.Bucket;
import lombok.experimental.SuperBuilder;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.bucket.range.ParsedRange;
import org.elasticsearch.search.aggregations.bucket.range.Range;

@SuperBuilder(toBuilder = true)
public class RangeAggregationParser extends BucketAggregationParser {

    @Override
    public RangeAggregation parse(final Aggregation result) {
        if (result instanceof ParsedRange range) {
            return parseRange(range);
        }
        throw new IllegalArgumentException("Expected ParsedRange, got '%s'".formatted(result));
    }

    private RangeAggregation parseRange(final ParsedRange range) {
        final var builder = RangeAggregation.builder()
                .name(range.getName());
        range.getBuckets().forEach(bucket -> builder.bucket(bucket.getKeyAsString(), parseBucket(bucket)));
        return builder.build();
    }

    private Bucket parseBucket(final Range.Bucket bucket) {
        final var builder = Bucket.builder()
                .from((double) bucket.getFrom())
                .to((double) bucket.getTo())
                .count(bucket.getDocCount());
        parseSubAggregations(bucket.getAggregations().asList())
                .forEach(result -> builder.aggregation(result.getName(), result));
        return builder.build();
    }
}
