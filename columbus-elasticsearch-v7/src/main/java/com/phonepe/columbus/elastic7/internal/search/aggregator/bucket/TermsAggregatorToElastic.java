package com.phonepe.columbus.elastic7.internal.search.aggregator.bucket;

import com.phonepe.columbus.elastic7.internal.search.utils.MiscTransforms;
import com.phonepe.columbus.elastic7.internal.search.utils.SearchEnums;
import com.phonepe.columbus.elastic7.search.aggregator.ElasticAggregatorTransformationContext;
import com.phonepe.columbus.registry.factory.RegistryKey;
import com.phonepe.columbus.search.aggregator.bucket.TermsAggregator;
import org.apache.commons.collections4.CollectionUtils;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;

import static com.phonepe.columbus.common.Predicates.applyIf;
import static com.phonepe.columbus.common.Predicates.applyIfNonNegative;
import static com.phonepe.columbus.common.Predicates.applyIfNotNull;
import static com.phonepe.columbus.common.Predicates.applyIfPositive;

@RegistryKey(name = TermsAggregator.TYPE, type = TermsAggregator.class)
public class TermsAggregatorToElastic<C extends ElasticAggregatorTransformationContext<C>>
        extends BucketAggregatorToElastic<TermsAggregator, C> {

    protected AggregationBuilder buildBucketAggregation(
            final TermsAggregator aggregator,
            final Object props,
            final C context
    ) {
        final var builder = AggregationBuilders.terms(aggregator.getName())
                .field(aggregator.getField());

        applyIfPositive(aggregator.getSize(), builder::size);
        applyIfPositive(aggregator.getShardSize(), builder::shardSize);

        applyIfNotNull(aggregator.getIncludeExclude(), MiscTransforms::toElastic, builder::includeExclude);

        final var bucketOrders = aggregator.getOrders().stream().map(MiscTransforms::toElastic).toList();
        applyIf(bucketOrders, CollectionUtils::isNotEmpty, builder::order);

        applyIfNonNegative(aggregator.getMinDocCount(), builder::minDocCount);
        applyIfNonNegative(aggregator.getShardMinDocCount(), builder::shardMinDocCount);

        applyIfNotNull(aggregator.getMissingValue(), builder::missing);
        applyIfNotNull(aggregator.getCollectMode(), SearchEnums::toSubAggregationCollectionMode, builder::collectMode);
        applyIfNotNull(aggregator.getShowCountError(), builder::showTermDocCountError);

        return builder;
    }
}
