package com.phonepe.commons.columbus.search.aggregator.bucket;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.phonepe.commons.columbus.search.misc.bucket.BucketSortOrder;
import lombok.Data;
import lombok.NonNull;
import lombok.Singular;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@Data
@Jacksonized
@SuperBuilder(toBuilder = true)
@JsonTypeName(HistogramAggregator.TYPE)
public class HistogramAggregator extends BucketAggregator {

    public static final String TYPE = "HISTOGRAM";

    private String field;

    @NonNull
    private IntervalType intervalType;
    /**
     * The width of each bucket. Depending on {@link #intervalType}, this can be a number or a date expression.
     */
    private String interval;
    /**
     * The shift of the bucket. Depending on {@link #intervalType}, this can be a number or a date expression.
     */
    private String offset;

    private Number minValue;
    private Number maxValue;
    /**
     * Histogram ranges are determined by the values in the data. The min and max values will restrict the range of
     * buckets to start at min and end at max (if provided). With soft bounds, the min and max values only guide the
     * range of the histogram. If there are values beyond the range, they will determine the bounds of the histogram.
     * Whether the buckets will be returned is still determined by {@link #minDocCount}.
     */
    private boolean softBounds;

    @Singular(ignoreNullCollections = true)
    private List<@NonNull BucketSortOrder> orders;

    /**
     * The minimum number of documents that should be present in a bucket for it to be included in the results.
     */
    private Integer minDocCount;
    private Object missingValue;

    @Override
    public @NonNull String getType() {
        return TYPE;
    }

    /**
     * Represents the type of interval used in the histogram.
     */
    public enum IntervalType {
        /**
         * For numeric data, expects a fixed value to define the width of the interval.
         */
        NUMERIC,
        /**
         * For dates, with a fixed duration - some days, hours, minutes, seconds or millis.
         */
        FIXED_DATE,
        /**
         * For dates, with fixed duration, or as per the calendar - supports weeks, months, quarters and years -
         * where the duration of buckets is not equal. Multiples of any units are not supported.
         */
        CALENDAR_DATE,
    }
}
