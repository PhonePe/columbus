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
package com.phonepe.columbus.search.aggregator.bucket;

import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.phonepe.columbus.search.misc.bucket.BucketSortOrder;
import lombok.Data;
import lombok.NonNull;
import lombok.Singular;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@JsonClassDescription("Bucket aggregator that groups documents into histogram buckets based on specified intervals")
@Data
@Jacksonized
@SuperBuilder(toBuilder = true)
@JsonTypeName(HistogramAggregator.TYPE)
public class HistogramAggregator extends BucketAggregator {

    public static final String TYPE = "HISTOGRAM";

    @JsonPropertyDescription("The field to use for histogram bucketing")
    private String field;

    @NonNull
    @JsonPropertyDescription("The type of interval used for bucketing")
    @JsonProperty(required = true)
    private IntervalType intervalType;

    @JsonPropertyDescription("The size of each bucket. Depending on intervalType, this can be a number or a date expression.")
    private String interval;

    @JsonPropertyDescription("The shift of the bucket. Depending on intervalType, this can be a number or a date expression.")
    private String offset;

    @JsonPropertyDescription("Minimum value for the histogram buckets")
    private Number minValue;

    @JsonPropertyDescription("Maximum value for the histogram buckets")
    private Number maxValue;

    @JsonPropertyDescription("Whether the min and max values are strict bounds or soft bounds. Histogram ranges are determined by the values in the data. The min and max values will restrict the range of buckets to start at min and end at max (if provided). With soft bounds, the min and max values only guide the range of the histogram. If there are values beyond the range, they will determine the bounds of the histogram. Whether the buckets will be returned is still determined by minDocCount.")
    private boolean softBounds;

    @Singular(ignoreNullCollections = true)
    @JsonPropertyDescription("List of sort orders to determine which top buckets are returned")
    private List<@NonNull BucketSortOrder> orders;

    @JsonPropertyDescription("Minimum number of documents that must be present in a bucket for it to be included in the results")
    private Integer minDocCount;

    @JsonPropertyDescription("Default value to use for documents where the field is missing")
    private Object missingValue;

    @Override
    public @NonNull String getType() {
        return TYPE;
    }

    @JsonClassDescription("Type of interval used for histogram bucketing")
    public enum IntervalType {
        @JsonPropertyDescription("For numeric data, expects a fixed value to define the width of the interval.")
        NUMERIC,
        @JsonPropertyDescription("For dates, with a fixed duration - some days, hours, minutes, seconds or millis.")
        FIXED_DATE,
        @JsonPropertyDescription("For dates, with fixed duration, or as per the calendar - supports weeks, months, quarters and years - where the duration of buckets is not equal. Multiples of any units are not supported.")
        CALENDAR_DATE,
    }
}
