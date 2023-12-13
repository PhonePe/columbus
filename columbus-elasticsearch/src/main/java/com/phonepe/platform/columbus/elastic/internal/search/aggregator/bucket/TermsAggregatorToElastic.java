package com.phonepe.platform.columbus.elastic.internal.search.aggregator.bucket;

import com.phonepe.platform.columbus.elastic.internal.search.utils.MiscTransforms;
import com.phonepe.platform.columbus.elastic.internal.search.utils.SearchEnums;
import com.phonepe.platform.columbus.elastic.search.aggregator.ElasticAggregatorTransformationContext;
import com.phonepe.platform.columbus.registry.factory.RegistryKey;
import com.phonepe.platform.columbus.search.aggregator.bucket.TermsAggregator;
import org.apache.commons.collections4.CollectionUtils;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;

import static com.phonepe.platform.columbus.common.Predicates.applyIf;
import static com.phonepe.platform.columbus.common.Predicates.applyIfNonNegative;
import static com.phonepe.platform.columbus.common.Predicates.applyIfNotNull;
import static com.phonepe.platform.columbus.common.Predicates.applyIfPositive;

@RegistryKey(name = TermsAggregator.TYPE, type = TermsAggregator.class)
public class TermsAggregatorToElastic<C extends ElasticAggregatorTransformationContext<C>>
        extends BucketAggregatorToElastic<TermsAggregator, C> {

    protected AggregationBuilder buildBucketAggregation(
            final TermsAggregator aggregation,
            final Object props,
            final C context
    ) {
        final var builder = AggregationBuilders.terms(aggregation.getName())
                .field(aggregation.getField());

        applyIfPositive(aggregation.getSize(), builder::size);
        applyIfPositive(aggregation.getShardSize(), builder::shardSize);

        applyIfNotNull(aggregation.getIncludeExclude(), MiscTransforms::toElastic, builder::includeExclude);

        final var bucketOrders = aggregation.getOrders().stream().map(MiscTransforms::toElastic).toList();
        applyIf(bucketOrders, CollectionUtils::isNotEmpty, builder::order);

        applyIfNonNegative(aggregation.getMinDocCount(), builder::minDocCount);
        applyIfNonNegative(aggregation.getShardMinDocCount(), builder::shardMinDocCount);

        applyIfNotNull(aggregation.getMissingValue(), builder::missing);
        applyIfNotNull(aggregation.getCollectMode(), SearchEnums::toSubAggregationCollectionMode, builder::collectMode);
        applyIfNotNull(aggregation.getShowCountError(), builder::showTermDocCountError);

        return builder;
    }
}
