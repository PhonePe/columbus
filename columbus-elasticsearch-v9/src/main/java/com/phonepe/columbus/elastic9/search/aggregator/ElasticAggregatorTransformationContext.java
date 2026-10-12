package com.phonepe.columbus.elastic9.search.aggregator;

import co.elastic.clients.elasticsearch._types.aggregations.Aggregation;
import com.phonepe.columbus.elastic9.storage.toolkit.ElasticToolkit;
import com.phonepe.columbus.search.aggregator.visitor.AggregatorTransformationContext;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

/**
 * The context used to transform {@link com.phonepe.columbus.search.aggregator.Aggregator Aggregators} as
 * needed by ElasticSearch.
 *
 * @param <C> self reference.
 */
@Getter
@Accessors(fluent = true)
@SuperBuilder(toBuilder = true)
public abstract class ElasticAggregatorTransformationContext<C extends ElasticAggregatorTransformationContext<C>>
        extends AggregatorTransformationContext<Aggregation, Object, C> {

    @NonNull
    private final ElasticToolkit toolkit;
}
