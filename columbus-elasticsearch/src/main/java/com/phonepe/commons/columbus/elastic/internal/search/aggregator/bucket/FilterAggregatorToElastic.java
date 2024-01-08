package com.phonepe.commons.columbus.elastic.internal.search.aggregator.bucket;

import com.phonepe.commons.columbus.elastic.search.aggregator.ElasticAggregatorTransformationContext;
import com.phonepe.commons.columbus.registry.factory.RegistryKey;
import com.phonepe.commons.columbus.search.aggregator.bucket.FilterAggregator;
import com.phonepe.commons.columbus.search.query.Query;
import com.phonepe.commons.columbus.search.query.simple.MatchAllQuery;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;

import java.util.Objects;

@Slf4j
@RegistryKey(name = FilterAggregator.TYPE, type = FilterAggregator.class)
public class FilterAggregatorToElastic<C extends ElasticAggregatorTransformationContext<C>>
        extends BucketAggregatorToElastic<FilterAggregator, C> {

    private static final Query DEFAULT_FILTER = MatchAllQuery.builder().build();

    protected AggregationBuilder buildBucketAggregation(
            final FilterAggregator aggregation,
            final Object props,
            final C context
    ) {
        if (aggregation.getFilter() == null) {
            log.warn("FilterAggregation(name={}) has no filter", aggregation.getName());
        }
        final var filter = Objects.requireNonNullElse(aggregation.getFilter(), DEFAULT_FILTER);
        return AggregationBuilders.filter(
                aggregation.getName(),
                context.toolkit().queryTransformer().transform(filter, props));
    }
}
