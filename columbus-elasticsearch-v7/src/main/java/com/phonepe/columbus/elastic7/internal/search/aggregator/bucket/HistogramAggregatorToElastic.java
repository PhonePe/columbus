package com.phonepe.columbus.elastic7.internal.search.aggregator.bucket;

import com.phonepe.columbus.elastic7.internal.search.utils.MiscTransforms;
import com.phonepe.columbus.elastic7.search.aggregator.ElasticAggregatorTransformationContext;
import com.phonepe.columbus.registry.factory.RegistryKey;
import com.phonepe.columbus.search.aggregator.bucket.HistogramAggregator;
import com.phonepe.columbus.search.aggregator.bucket.HistogramAggregator.IntervalType;
import org.apache.commons.collections4.CollectionUtils;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.BucketOrder;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramInterval;
import org.elasticsearch.search.aggregations.bucket.histogram.DoubleBounds;
import org.elasticsearch.search.aggregations.bucket.histogram.HistogramAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.histogram.LongBounds;

import java.util.List;

import static com.phonepe.columbus.common.Predicates.applyIf;
import static com.phonepe.columbus.common.Predicates.applyIfNonNegative;
import static com.phonepe.columbus.common.Predicates.applyIfNotNull;

@RegistryKey(name = HistogramAggregator.TYPE, type = HistogramAggregator.class)
public class HistogramAggregatorToElastic<C extends ElasticAggregatorTransformationContext<C>>
        extends BucketAggregatorToElastic<HistogramAggregator, C> {

    protected AggregationBuilder buildBucketAggregation(
            final HistogramAggregator aggregator,
            final Object props,
            final C context
    ) {
        if (IntervalType.NUMERIC.equals(aggregator.getIntervalType())) {
            return buildNumericHistogram(aggregator);
        }
        return buildDateHistogram(aggregator);
    }

    private HistogramAggregationBuilder buildNumericHistogram(final HistogramAggregator aggregator) {
        final var builder = AggregationBuilders.histogram(aggregator.getName())
                .field(aggregator.getField());

        applyIfNotNull(aggregator.getInterval(), Double::parseDouble, builder::interval);
        applyIfNotNull(aggregator.getOffset(), Double::parseDouble, builder::offset);

        if (hasBounds(aggregator)) {
            final var bounds = new DoubleBounds(toDouble(aggregator.getMinValue()), toDouble(aggregator.getMaxValue()));
            if (aggregator.isSoftBounds()) {
                builder.extendedBounds(bounds);
            } else {
                builder.hardBounds(bounds);
            }
        }

        applyIf(getBucketOrders(aggregator), CollectionUtils::isNotEmpty, builder::order);

        applyIfNonNegative(aggregator.getMinDocCount(), builder::minDocCount);
        applyIfNotNull(aggregator.getMissingValue(), builder::missing);

        return builder;
    }

    private DateHistogramAggregationBuilder buildDateHistogram(final HistogramAggregator aggregator) {
        final var builder = AggregationBuilders.dateHistogram(aggregator.getName())
                .field(aggregator.getField());

        applyIfNotNull(
                aggregator.getInterval(),
                DateHistogramInterval::new,
                IntervalType.FIXED_DATE.equals(aggregator.getIntervalType())
                        ? builder::fixedInterval
                        : builder::calendarInterval);
        applyIfNotNull(aggregator.getOffset(), builder::offset);

        if (hasBounds(aggregator)) {
            final var bounds = new LongBounds(toLong(aggregator.getMinValue()), toLong(aggregator.getMaxValue()));
            if (aggregator.isSoftBounds()) {
                builder.extendedBounds(bounds);
            } else {
                builder.hardBounds(bounds);
            }
        }

        applyIf(getBucketOrders(aggregator), CollectionUtils::isNotEmpty, builder::order);

        applyIfNonNegative(aggregator.getMinDocCount(), builder::minDocCount);
        applyIfNotNull(aggregator.getMissingValue(), builder::missing);

        return builder;
    }

    private List<BucketOrder> getBucketOrders(final HistogramAggregator aggregator) {
        return aggregator.getOrders().stream().map(MiscTransforms::toElastic).toList();
    }

    private boolean hasBounds(final HistogramAggregator aggregator) {
        return aggregator.getMinValue() != null || aggregator.getMaxValue() != null;
    }

    private Double toDouble(final Number number) {
        return number == null ? null : number.doubleValue();
    }

    private Long toLong(final Number number) {
        return number == null ? null : number.longValue();
    }
}
