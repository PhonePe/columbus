package com.phonepe.commons.columbus.elastic9.internal.search.aggregator.bucket;

import co.elastic.clients.elasticsearch._types.aggregations.Aggregation;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryVariant;
import com.phonepe.commons.columbus.elastic9.search.aggregator.ElasticAggregatorTransformationContext;
import com.phonepe.commons.columbus.registry.factory.RegistryKey;
import com.phonepe.commons.columbus.search.aggregator.bucket.FilterAggregator;
import com.phonepe.commons.columbus.search.query.Query;
import com.phonepe.commons.columbus.search.query.simple.MatchAllQuery;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

@Slf4j
@RegistryKey(name = FilterAggregator.TYPE, type = FilterAggregator.class)
public class FilterAggregatorToElastic<C extends ElasticAggregatorTransformationContext<C>>
        extends BucketAggregatorToElastic<FilterAggregator, C> {

    private static final Query DEFAULT_FILTER = MatchAllQuery.builder().build();

    protected Aggregation.Builder.ContainerBuilder buildBucketAggregation(
            final FilterAggregator aggregator,
            final Object props,
            final C context
    ) {
        if (aggregator.getFilter() == null) {
            log.warn("FilterAggregator(name={}) has no filter", aggregator.getName());
        }
        final var filter = Objects.requireNonNullElse(aggregator.getFilter(), DEFAULT_FILTER);

        final QueryVariant query = context.toolkit().queryTransformer().transform(filter, props);

        return new Aggregation.Builder().filter(query._toQuery());
    }
}
