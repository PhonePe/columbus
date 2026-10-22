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

import com.phonepe.columbus.search.aggregation.bucket.RangeAggregation;
import com.phonepe.columbus.search.aggregation.bucket.RangeAggregation.Bucket;
import lombok.experimental.SuperBuilder;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.bucket.range.ParsedRange;
import org.elasticsearch.search.aggregations.bucket.range.Range;

@SuperBuilder(toBuilder = true)
public class RangeAggregationParser extends BucketAggregationParser {

    @Override
    public RangeAggregation parse(final Aggregation result) {
        if (result instanceof ParsedRange range) {
            return parseRange(range);
        }
        throw new IllegalArgumentException("Expected ParsedRange, got '%s', with name '%s'".formatted(
                result.getClass().getSimpleName(), result.getName()));
    }

    private RangeAggregation parseRange(final ParsedRange range) {
        final var builder = RangeAggregation.builder()
                .name(range.getName());
        range.getBuckets().forEach(bucket -> builder.bucket(bucket.getKeyAsString(), parseBucket(bucket)));
        return builder.build();
    }

    private Bucket parseBucket(final Range.Bucket bucket) {
        final var builder = Bucket.builder()
                .from((double) bucket.getFrom())
                .to((double) bucket.getTo())
                .count(bucket.getDocCount());
        parseSubAggregations(bucket.getAggregations().asList())
                .forEach(result -> builder.aggregation(result.getName(), result));
        return builder.build();
    }
}
