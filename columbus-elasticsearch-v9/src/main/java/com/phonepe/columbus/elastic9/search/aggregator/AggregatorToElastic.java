package com.phonepe.columbus.elastic9.search.aggregator;

import co.elastic.clients.elasticsearch._types.aggregations.Aggregation;
import com.phonepe.columbus.search.aggregator.Aggregator;
import com.phonepe.columbus.search.aggregator.visitor.AggregatorTransformationVisitor;

/**
 * An interface to transform a specific {@link Aggregator} to its internal representation for ElasticSearch.
 *
 * @param <T> the type of the {@link Aggregator} being transformed.
 * @param <C> the type of the context required for transformation.
 */
public interface AggregatorToElastic<T extends Aggregator, C extends ElasticAggregatorTransformationContext<C>>
        extends AggregatorTransformationVisitor<Aggregation, T, Object, C> {
}
