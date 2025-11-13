package com.phonepe.commons.columbus.elastic9.internal.search.aggregator.bucket;

import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.aggregations.Aggregation;
import co.elastic.clients.elasticsearch._types.aggregations.TermsAggregation.Builder;
import com.phonepe.commons.columbus.elastic9.internal.search.utils.MiscTransforms;
import com.phonepe.commons.columbus.elastic9.internal.search.utils.SearchEnums;
import com.phonepe.commons.columbus.elastic9.search.aggregator.ElasticAggregatorTransformationContext;
import com.phonepe.commons.columbus.registry.factory.RegistryKey;
import com.phonepe.commons.columbus.search.aggregator.bucket.TermsAggregator;
import org.apache.commons.collections4.CollectionUtils;

import static com.phonepe.commons.columbus.common.Predicates.applyIf;
import static com.phonepe.commons.columbus.common.Predicates.applyIfNonNegative;
import static com.phonepe.commons.columbus.common.Predicates.applyIfNotNull;
import static com.phonepe.commons.columbus.common.Predicates.applyIfPositive;

@RegistryKey(name = TermsAggregator.TYPE, type = TermsAggregator.class)
public class TermsAggregatorToElastic<C extends ElasticAggregatorTransformationContext<C>>
        extends BucketAggregatorToElastic<TermsAggregator, C> {

    protected Aggregation.Builder.ContainerBuilder buildBucketAggregation(
            final TermsAggregator aggregator,
            final Object props,
            final C context
    ) {
        final var builder = new Builder()
                .field(aggregator.getField());

        applyIfPositive(aggregator.getSize(), builder::size);
        applyIfPositive(aggregator.getShardSize(), builder::shardSize);

        applyIfNotNull(aggregator.getIncludeExclude(), MiscTransforms::toElasticTermsInclude, builder::include);
        applyIfNotNull(aggregator.getIncludeExclude(), MiscTransforms::toElasticTermsExclude, builder::exclude);

        final var bucketOrders = aggregator.getOrders().stream().map(MiscTransforms::toElastic).toList();
        applyIf(bucketOrders, CollectionUtils::isNotEmpty, builder::order);

        applyIfNonNegative(aggregator.getMinDocCount(), builder::minDocCount);

        if (aggregator.getShardMinDocCount() != null) {
            builder.shardMinDocCount((long) aggregator.getShardMinDocCount());
        }
        applyIfNotNull(aggregator.getMissingValue(), FieldValue::of, builder::missing);
        applyIfNotNull(aggregator.getCollectMode(), SearchEnums::toSubAggregationCollectionMode, builder::collectMode);
        applyIfNotNull(aggregator.getShowCountError(), builder::showTermDocCountError);

        return new Aggregation.Builder().terms(builder.build());
    }
}
