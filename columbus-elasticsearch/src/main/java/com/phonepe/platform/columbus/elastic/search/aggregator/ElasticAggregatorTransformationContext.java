package com.phonepe.platform.columbus.elastic.search.aggregator;

import com.phonepe.platform.columbus.elastic.storage.toolkit.ElasticToolkit;
import com.phonepe.platform.columbus.search.aggregator.visitor.AggregatorTransformationContext;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilder;

/**
 * The context used to transform {@link com.phonepe.platform.columbus.search.aggregator.Aggregator Aggregators} as
 * needed by ElasticSearch.
 *
 * @param <C> self reference.
 */
@Getter
@Accessors(fluent = true)
@SuperBuilder(toBuilder = true)
public abstract class ElasticAggregatorTransformationContext<C extends ElasticAggregatorTransformationContext<C>>
        extends AggregatorTransformationContext<AggregationBuilder, C> {

    @NonNull
    private final ElasticToolkit toolkit;
}
