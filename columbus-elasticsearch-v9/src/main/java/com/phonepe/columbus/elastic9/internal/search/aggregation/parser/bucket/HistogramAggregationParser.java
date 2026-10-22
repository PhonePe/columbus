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
package com.phonepe.columbus.elastic9.internal.search.aggregation.parser.bucket;

import co.elastic.clients.elasticsearch._types.aggregations.Aggregate;
import co.elastic.clients.elasticsearch._types.aggregations.DateHistogramAggregate;
import co.elastic.clients.elasticsearch._types.aggregations.DateHistogramBucket;
import co.elastic.clients.elasticsearch._types.aggregations.HistogramAggregate;
import co.elastic.clients.elasticsearch._types.aggregations.HistogramBucket;
import com.phonepe.columbus.search.aggregation.bucket.HistogramAggregation;
import com.phonepe.columbus.search.aggregation.bucket.HistogramAggregation.Bucket;
import lombok.experimental.SuperBuilder;

import java.util.Collection;

@SuperBuilder(toBuilder = true)
public class HistogramAggregationParser extends BucketAggregationParser {

    @Override
    public HistogramAggregation parse(final Aggregate result, final String name) {
        if (result.isHistogram()) {
            return parseHistogram(result.histogram(), name);
        }
        if (result.isDateHistogram()) {
            return parseDateHistogram(result.dateHistogram(), name);
        }
        throw new IllegalArgumentException("Expected (Date)Histogram, got '%s', with name '%s'".formatted(
                result.getClass().getSimpleName(), name));
    }

    private HistogramAggregation parseHistogram(final HistogramAggregate histogram, final String name) {
        final var builder = HistogramAggregation.builder()
                .name(name);
        final Collection<HistogramBucket> buckets = histogram.buckets().isArray()
                ? histogram.buckets().array() : histogram.buckets().keyed().values();
        buckets.forEach(bucket -> builder.bucket(bucket.keyAsString(), parseBucket(bucket)));
        return builder.build();
    }

    private HistogramAggregation parseDateHistogram(final DateHistogramAggregate histogram, final String name) {
        final var builder = HistogramAggregation.builder()
                .name(name);
        final Collection<DateHistogramBucket> buckets = histogram.buckets().isArray()
                ? histogram.buckets().array() : histogram.buckets().keyed().values();
        buckets.forEach(bucket -> builder.bucket(bucket.keyAsString(), parseDateBucket(bucket)));
        return builder.build();
    }

    private Bucket parseBucket(final HistogramBucket bucket) {
        final var builder = Bucket.builder()
                .key(bucket.key())
                .count(bucket.docCount());
        parseSubAggregations(bucket.aggregations())
                .forEach(result -> builder.aggregation(result.getName(), result));
        return builder.build();
    }

    private Bucket parseDateBucket(final DateHistogramBucket bucket) {
        final var builder = Bucket.builder()
                .key(bucket.key())
                .count(bucket.docCount());
        parseSubAggregations(bucket.aggregations())
                .forEach(result -> builder.aggregation(result.getName(), result));
        return builder.build();
    }
}
