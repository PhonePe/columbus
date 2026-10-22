/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.phonepe.columbus.elastic9.internal.search.aggregator.bucket;

import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.aggregations.Aggregation;
import co.elastic.clients.elasticsearch._types.aggregations.TermsAggregation.Builder;
import com.phonepe.columbus.elastic9.internal.search.utils.MiscTransforms;
import com.phonepe.columbus.elastic9.internal.search.utils.SearchEnums;
import com.phonepe.columbus.elastic9.search.aggregator.ElasticAggregatorTransformationContext;
import com.phonepe.columbus.registry.factory.RegistryKey;
import com.phonepe.columbus.search.aggregator.bucket.TermsAggregator;
import org.apache.commons.collections4.CollectionUtils;

import static com.phonepe.columbus.common.Predicates.applyIf;
import static com.phonepe.columbus.common.Predicates.applyIfNonNegative;
import static com.phonepe.columbus.common.Predicates.applyIfNotNull;
import static com.phonepe.columbus.common.Predicates.applyIfPositive;

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
