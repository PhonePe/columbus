package com.phonepe.platform.columbus.elastic.internal.search.aggregation.parser.metrics;

import com.phonepe.platform.columbus.elastic.internal.search.aggregation.parser.ElasticAggregationParser;
import com.phonepe.platform.columbus.elastic.internal.search.utils.MiscTransforms;
import com.phonepe.platform.columbus.search.aggregation.metrics.TopHitsAggregation;
import com.phonepe.platform.columbus.serde.SerDe;
import com.phonepe.platform.columbus.type.EntityType;
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
        throw new IllegalArgumentException("Expected ParsedTopHits, got '%s'".formatted(result));
    }

    private TopHitsAggregation<T> parseStats(final ParsedTopHits topHits) {
        return TopHitsAggregation.<T>builder()
                .name(topHits.getName())
                .topHits(MiscTransforms.fromElastic(topHits.getHits(), entityType, serDe))
                .build();
    }
}
