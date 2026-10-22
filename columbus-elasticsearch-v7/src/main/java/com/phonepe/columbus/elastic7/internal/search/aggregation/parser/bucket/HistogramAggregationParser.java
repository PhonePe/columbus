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
package com.phonepe.columbus.elastic7.internal.search.aggregation.parser.bucket;

import com.phonepe.columbus.search.aggregation.bucket.HistogramAggregation;
import com.phonepe.columbus.search.aggregation.bucket.HistogramAggregation.Bucket;
import lombok.experimental.SuperBuilder;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.bucket.histogram.Histogram;
import org.elasticsearch.search.aggregations.bucket.histogram.ParsedDateHistogram;
import org.elasticsearch.search.aggregations.bucket.histogram.ParsedHistogram;

import java.time.ZonedDateTime;

@SuperBuilder(toBuilder = true)
public class HistogramAggregationParser extends BucketAggregationParser {

    @Override
    public HistogramAggregation parse(final Aggregation result) {
        if (result instanceof ParsedHistogram histogram) {
            return parseHistogram(histogram);
        }
        if (result instanceof ParsedDateHistogram histogram) {
            return parseDateHistogram(histogram);
        }
        throw new IllegalArgumentException("Expected Parsed(Date)Histogram, got '%s', with name '%s'".formatted(
                result.getClass().getSimpleName(), result.getName()));
    }

    private HistogramAggregation parseHistogram(final ParsedHistogram histogram) {
        final var builder = HistogramAggregation.builder()
                .name(histogram.getName());
        histogram.getBuckets().forEach(bucket -> builder.bucket(bucket.getKeyAsString(), parseBucket(bucket)));
        return builder.build();
    }

    private HistogramAggregation parseDateHistogram(final ParsedDateHistogram histogram) {
        final var builder = HistogramAggregation.builder()
                .name(histogram.getName());
        histogram.getBuckets().forEach(bucket -> builder.bucket(bucket.getKeyAsString(), parseDateBucket(bucket)));
        return builder.build();
    }

    private Bucket parseBucket(final Histogram.Bucket bucket) {
        final var builder = Bucket.builder()
                .key((Number) bucket.getKey())
                .count(bucket.getDocCount());
        parseSubAggregations(bucket.getAggregations().asList())
                .forEach(result -> builder.aggregation(result.getName(), result));
        return builder.build();
    }

    private Bucket parseDateBucket(final Histogram.Bucket bucket) {
        final var builder = Bucket.builder()
                .key(((ZonedDateTime) bucket.getKey()).toInstant().toEpochMilli())
                .count(bucket.getDocCount());
        parseSubAggregations(bucket.getAggregations().asList())
                .forEach(result -> builder.aggregation(result.getName(), result));
        return builder.build();
    }
}
