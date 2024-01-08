package com.phonepe.commons.columbus.elastic.internal.search.aggregation.parser.metrics;

import com.phonepe.commons.columbus.elastic.internal.search.aggregation.parser.ElasticAggregationParser;
import com.phonepe.commons.columbus.elastic.internal.search.utils.MiscTransforms;
import com.phonepe.commons.columbus.search.aggregation.metrics.TopHitsAggregation;
import com.phonepe.commons.columbus.serde.SerDe;
import com.phonepe.commons.columbus.type.EntityType;
import lombok.Builder;
import lombok.NonNull;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.metrics.ParsedTopHits;

@Builder
public class TopHitsAggregationParser<T> implements ElasticAggregationParser {

    @NonNull
    private final EntityType<T> entityType;
    @NonNull
    private final SerDe serDe;

    @Override
    public TopHitsAggregation<T> parse(final Aggregation result) {
        if (result instanceof ParsedTopHits topHits) {
            return parseStats(topHits);
        }
        throw new IllegalArgumentException("Expected ParsedTopHits, got '%s', with name '%s'".formatted(
                result.getClass().getSimpleName(), result.getName()));
    }

    private TopHitsAggregation<T> parseStats(final ParsedTopHits topHits) {
        return TopHitsAggregation.<T>builder()
                .name(topHits.getName())
                .topHits(MiscTransforms.fromElastic(topHits.getHits(), entityType, serDe))
                .build();
    }
}
