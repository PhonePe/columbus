package com.phonepe.columbus.elastic7.internal.search.aggregator.bucket;

import com.phonepe.columbus.elastic7.search.aggregator.ElasticAggregatorTransformationContext;
import com.phonepe.columbus.registry.factory.RegistryKey;
import com.phonepe.columbus.search.aggregator.bucket.FilterAggregator;
import com.phonepe.columbus.search.query.Query;
import com.phonepe.columbus.search.query.simple.MatchAllQuery;
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
            final FilterAggregator aggregator,
            final Object props,
            final C context
    ) {
        if (aggregator.getFilter() == null) {
            log.warn("FilterAggregator(name={}) has no filter", aggregator.getName());
        }
        final var filter = Objects.requireNonNullElse(aggregator.getFilter(), DEFAULT_FILTER);
        return AggregationBuilders.filter(
                aggregator.getName(),
                context.toolkit().queryTransformer().transform(filter, props));
    }
}
