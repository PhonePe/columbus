package com.phonepe.commons.columbus.elastic9.search.aggregation;

import co.elastic.clients.elasticsearch._types.aggregations.Aggregate;
import com.phonepe.commons.columbus.elastic9.storage.toolkit.ElasticToolkit;
import com.phonepe.commons.columbus.search.aggregation.AggregationParser;
import com.phonepe.commons.columbus.search.aggregation.visitor.AggregationParserResolutionContext;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

/**
 * The context used to resolve an {@link AggregationParser} for parsing ElasticSearch's aggregation result.
 *
 * @param <C> self reference.
 */
@Getter
@Accessors(fluent = true)
@SuperBuilder(toBuilder = true)
public abstract class ElasticAggregationParserResolutionContext<C extends ElasticAggregationParserResolutionContext<C>>
        extends AggregationParserResolutionContext<Aggregate, Object, C> {

    @NonNull
    private final ElasticToolkit toolkit;
}
