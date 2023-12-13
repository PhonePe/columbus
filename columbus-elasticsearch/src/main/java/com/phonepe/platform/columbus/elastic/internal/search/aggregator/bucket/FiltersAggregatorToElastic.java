package com.phonepe.platform.columbus.elastic.internal.search.aggregator.bucket;

import com.phonepe.platform.columbus.elastic.search.aggregator.ElasticAggregatorTransformationContext;
import com.phonepe.platform.columbus.registry.factory.RegistryKey;
import com.phonepe.platform.columbus.search.aggregator.bucket.FiltersAggregator;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.filter.FiltersAggregator.KeyedFilter;

import static com.phonepe.platform.columbus.common.Predicates.applyIfNotNull;

@Slf4j
@RegistryKey(name = FiltersAggregator.TYPE, type = FiltersAggregator.class)
public class FiltersAggregatorToElastic<C extends ElasticAggregatorTransformationContext<C>>
        extends BucketAggregatorToElastic<FiltersAggregator, C> {

    protected AggregationBuilder buildBucketAggregation(
            final FiltersAggregator aggregation,
            final Object props,
            final C context
    ) {
        final var queryTransformer = context.toolkit().queryTransformer();
        final var filters = aggregation.getFilters().entrySet().stream()
                .map(entry -> new KeyedFilter(entry.getKey(), queryTransformer.transform(entry.getValue(), props)))
                .toArray(KeyedFilter[]::new);

        final var builder = AggregationBuilders.filters(aggregation.getName(), filters);
        applyIfNotNull(aggregation.getOtherBucketKey(), key -> builder.otherBucket(true).otherBucketKey(key));
        return builder;
    }
}
