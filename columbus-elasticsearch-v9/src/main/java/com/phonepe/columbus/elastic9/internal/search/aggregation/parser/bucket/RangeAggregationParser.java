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
import co.elastic.clients.elasticsearch._types.aggregations.RangeAggregate;
import co.elastic.clients.elasticsearch._types.aggregations.RangeBucket;
import com.phonepe.columbus.search.aggregation.bucket.RangeAggregation;
import com.phonepe.columbus.search.aggregation.bucket.RangeAggregation.Bucket;
import lombok.experimental.SuperBuilder;

import java.util.Collection;

@SuperBuilder(toBuilder = true)
public class RangeAggregationParser extends BucketAggregationParser {

    @Override
    public RangeAggregation parse(final Aggregate result, final String name) {
        if (result.isRange()) {
            return parseRange(result.range(), name);
        }
        throw new IllegalArgumentException("Expected RangeAggregate, got '%s', with name '%s'".formatted(
                result.getClass().getSimpleName(), result._kind()));
    }

    private RangeAggregation parseRange(final RangeAggregate range, final String name) {
        final var builder = RangeAggregation.builder()
                .name(name);
        final Collection<RangeBucket> buckets = range.buckets().isArray()
                ? range.buckets().array() : range.buckets().keyed().values();
        buckets.forEach(bucket -> builder.bucket(bucket.key(), parseBucket(bucket)));
        return builder.build();
    }

    private Bucket parseBucket(final RangeBucket bucket) {
        final var builder = Bucket.builder()
                .from(bucket.from())
                .to(bucket.to())
                .count(bucket.docCount());
        parseSubAggregations(bucket.aggregations())
                .forEach(result -> builder.aggregation(result.getName(), result));
        return builder.build();
    }
}
