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

import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.aggregations.Aggregation;
import co.elastic.clients.elasticsearch._types.aggregations.CalendarInterval;
import co.elastic.clients.elasticsearch._types.aggregations.DateHistogramAggregation;
import co.elastic.clients.elasticsearch._types.aggregations.ExtendedBounds;
import co.elastic.clients.elasticsearch._types.aggregations.FieldDateMath;
import co.elastic.clients.elasticsearch._types.aggregations.HistogramAggregation;
import co.elastic.clients.util.DateTime;
import co.elastic.clients.util.NamedValue;
import com.phonepe.columbus.elastic9.internal.search.utils.MiscTransforms;
import com.phonepe.columbus.elastic9.search.aggregator.ElasticAggregatorTransformationContext;
import com.phonepe.columbus.registry.factory.RegistryKey;
import com.phonepe.columbus.search.aggregator.bucket.HistogramAggregator;
import com.phonepe.columbus.search.aggregator.bucket.HistogramAggregator.IntervalType;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;

import static com.phonepe.columbus.common.Predicates.applyIf;
import static com.phonepe.columbus.common.Predicates.applyIfNonNegative;
import static com.phonepe.columbus.common.Predicates.applyIfNotNull;

@RegistryKey(name = HistogramAggregator.TYPE, type = HistogramAggregator.class)
public class HistogramAggregatorToElastic<C extends ElasticAggregatorTransformationContext<C>>
        extends BucketAggregatorToElastic<HistogramAggregator, C> {

    protected Aggregation.Builder.ContainerBuilder buildBucketAggregation(
            final HistogramAggregator aggregator,
            final Object props,
            final C context
    ) {
        if (IntervalType.NUMERIC.equals(aggregator.getIntervalType())) {
            return buildNumericHistogram(aggregator);
        }
        return buildDateHistogram(aggregator);
    }

    private Aggregation.Builder.ContainerBuilder buildNumericHistogram(final HistogramAggregator aggregator) {
        final var builder = new HistogramAggregation.Builder()
                .field(aggregator.getField());

        applyIfNotNull(aggregator.getInterval(), Double::parseDouble, builder::interval);
        applyIfNotNull(aggregator.getOffset(), Double::parseDouble, builder::offset);

        if (hasBounds(aggregator)) {
            final var bounds = new ExtendedBounds.Builder<Double>()
                    .min(toDouble(aggregator.getMinValue()))
                    .max(toDouble(aggregator.getMaxValue()))
                    .build();
            if (aggregator.isSoftBounds()) {
                builder.extendedBounds(bounds);
            } else {
                builder.hardBounds(bounds);
            }
        }

        applyIf(getBucketOrders(aggregator), CollectionUtils::isNotEmpty, builder::order);

        applyIfNonNegative(aggregator.getMinDocCount(), builder::minDocCount);

        if (aggregator.getMissingValue() instanceof Number numValue) {
            builder.missing(numValue.doubleValue());
        }

        return new Aggregation.Builder().histogram(builder.build());
    }

    private Aggregation.Builder.ContainerBuilder buildDateHistogram(final HistogramAggregator aggregator) {
        final var builder = new DateHistogramAggregation.Builder()
                .field(aggregator.getField());

        if (IntervalType.FIXED_DATE.equals(aggregator.getIntervalType())) {
            applyIfNotNull(aggregator.getInterval(), MiscTransforms::toElasticTime, builder::fixedInterval);
        } else {
            applyIfNotNull(aggregator.getInterval(), CalendarInterval::valueOf, builder::calendarInterval);
        }

        applyIfNotNull(aggregator.getOffset(), MiscTransforms::toElasticTime, builder::offset);

        if (hasBounds(aggregator)) {
            final var bounds = new ExtendedBounds.Builder<FieldDateMath>();
            applyIfNotNull(aggregator.getMinValue(), minValue ->
                    bounds.min(new FieldDateMath.Builder().value(toDouble(aggregator.getMinValue())).build()));
            applyIfNotNull(aggregator.getMaxValue(), maxValue ->
                    bounds.max(new FieldDateMath.Builder().value(toDouble(aggregator.getMaxValue())).build()));
            if (aggregator.isSoftBounds()) {
                builder.extendedBounds(bounds.build());
            } else {
                builder.hardBounds(bounds.build());
            }
        }

        applyIf(getBucketOrders(aggregator), CollectionUtils::isNotEmpty, builder::order);

        applyIfNonNegative(aggregator.getMinDocCount(), builder::minDocCount);

        if (aggregator.getMissingValue() != null) {
            if (aggregator.getMissingValue() instanceof final Number numValue) {
                builder.missing(DateTime.ofEpochMilli(numValue.longValue()));
            } else if (aggregator.getMissingValue() instanceof final String stringValue) {
                builder.missing(DateTime.of(stringValue));
            }
        }

        return new Aggregation.Builder().dateHistogram(builder.build());
    }

    private List<NamedValue<SortOrder>> getBucketOrders(final HistogramAggregator aggregator) {
        return aggregator.getOrders().stream().map(MiscTransforms::toElastic).toList();
    }

    private boolean hasBounds(final HistogramAggregator aggregator) {
        return aggregator.getMinValue() != null || aggregator.getMaxValue() != null;
    }

    private Double toDouble(final Number number) {
        return number == null ? null : number.doubleValue();
    }
}
