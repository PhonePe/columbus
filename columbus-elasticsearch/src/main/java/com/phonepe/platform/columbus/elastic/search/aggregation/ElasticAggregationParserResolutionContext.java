package com.phonepe.platform.columbus.elastic.search.aggregation;

import com.phonepe.platform.columbus.elastic.storage.toolkit.ElasticToolkit;
import com.phonepe.platform.columbus.search.aggregation.AggregationParser;
import com.phonepe.platform.columbus.search.aggregation.visitor.AggregationParserResolutionContext;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;
import org.elasticsearch.search.aggregations.Aggregation;

/**
 * The context used to resolve an {@link AggregationParser} for parsing ElasticSearch's aggregation result.
 *
 * @param <C> self reference.
 */
@Getter
@Accessors(fluent = true)
@SuperBuilder(toBuilder = true)
public abstract class ElasticAggregationParserResolutionContext<C extends ElasticAggregationParserResolutionContext<C>>
        extends AggregationParserResolutionContext<Aggregation, C> {

    @NonNull
    private final ElasticToolkit toolkit;
}
