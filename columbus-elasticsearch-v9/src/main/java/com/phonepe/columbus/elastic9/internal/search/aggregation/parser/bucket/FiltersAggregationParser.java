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
import co.elastic.clients.elasticsearch._types.aggregations.FiltersAggregate;
import co.elastic.clients.elasticsearch._types.aggregations.FiltersBucket;
import com.phonepe.columbus.search.aggregation.bucket.FiltersAggregation;
import com.phonepe.columbus.search.aggregation.bucket.FiltersAggregation.Bucket;
import lombok.experimental.SuperBuilder;

import java.util.Collection;

@SuperBuilder(toBuilder = true)
public class FiltersAggregationParser extends BucketAggregationParser {

    @Override
    public FiltersAggregation parse(final Aggregate result, final String name) {
        if (result.isFilters()) {
            return parseTerms(result.filters(), name);
        }
        throw new IllegalArgumentException("Expected FiltersAggregate, got '%s', with name '%s'".formatted(
                result.getClass().getSimpleName(), name));
    }

    private FiltersAggregation parseTerms(final FiltersAggregate filters, final String name) {
        final var builder = FiltersAggregation.builder()
                .name(name);
        final Collection<FiltersBucket> buckets = filters.buckets().isArray()
                ? filters.buckets().array() : filters.buckets().keyed().values();

        buckets.forEach(bucket -> builder.bucket(bucket.key(), parseBucket(bucket)));
        return builder.build();
    }

    private Bucket parseBucket(final FiltersBucket bucket) {
        final var builder = Bucket.builder()
                .count(bucket.docCount());
        parseSubAggregations(bucket.aggregations())
                .forEach(result -> builder.aggregation(result.getName(), result));
        return builder.build();
    }
}
