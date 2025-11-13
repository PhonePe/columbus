package com.phonepe.commons.columbus.elastic9.internal.search.aggregator.bucket;

import co.elastic.clients.elasticsearch._types.aggregations.Aggregation;
import co.elastic.clients.elasticsearch._types.aggregations.Buckets;
import co.elastic.clients.elasticsearch._types.aggregations.FiltersAggregation;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import com.phonepe.commons.columbus.elastic9.search.aggregator.ElasticAggregatorTransformationContext;
import com.phonepe.commons.columbus.registry.factory.RegistryKey;
import com.phonepe.commons.columbus.search.aggregator.bucket.FiltersAggregator;
import lombok.extern.slf4j.Slf4j;

import java.util.Map.Entry;
import java.util.stream.Collectors;

import static com.phonepe.commons.columbus.common.Predicates.applyIfNotNull;

@Slf4j
@RegistryKey(name = FiltersAggregator.TYPE, type = FiltersAggregator.class)
public class FiltersAggregatorToElastic<C extends ElasticAggregatorTransformationContext<C>>
        extends BucketAggregatorToElastic<FiltersAggregator, C> {

    protected Aggregation.Builder.ContainerBuilder buildBucketAggregation(
            final FiltersAggregator aggregator,
            final Object props,
            final C context
    ) {
        final var queryTransformer = context.toolkit().queryTransformer();
        final var filters = aggregator.getFilters().entrySet().stream()
                .collect(Collectors.toMap(Entry::getKey,
                        entry -> queryTransformer.transform(entry.getValue(), props)._toQuery()));

        final var builder = new FiltersAggregation.Builder().filters(new Buckets.Builder<Query>().keyed(filters).build());
        applyIfNotNull(aggregator.getOtherBucketKey(), key -> builder.otherBucket(true).otherBucketKey(key));

        return new Aggregation.Builder().filters(builder.build());
    }
}
