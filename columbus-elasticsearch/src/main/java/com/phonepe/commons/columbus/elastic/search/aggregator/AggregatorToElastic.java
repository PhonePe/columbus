package com.phonepe.commons.columbus.elastic.search.aggregator;

import com.phonepe.commons.columbus.search.aggregator.Aggregator;
import com.phonepe.commons.columbus.search.aggregator.visitor.AggregatorTransformationVisitor;
import org.elasticsearch.search.aggregations.AggregationBuilder;

/**
 * An interface to transform a specific {@link Aggregator} to its internal representation for ElasticSearch.
 *
 * @param <T> the type of the {@link Aggregator} being transformed.
 * @param <C> the type of the context required for transformation.
 */
public interface AggregatorToElastic<T extends Aggregator, C extends ElasticAggregatorTransformationContext<C>>
        extends AggregatorTransformationVisitor<AggregationBuilder, T, Object, C> {
}
