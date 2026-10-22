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
import co.elastic.clients.elasticsearch._types.aggregations.StringTermsAggregate;
import co.elastic.clients.elasticsearch._types.aggregations.StringTermsBucket;
import com.phonepe.columbus.search.aggregation.bucket.TermsAggregation;
import com.phonepe.columbus.search.aggregation.bucket.TermsAggregation.Bucket;
import lombok.experimental.SuperBuilder;

import java.util.Collection;

import static com.phonepe.columbus.common.Predicates.applyIfNotNull;

@SuperBuilder(toBuilder = true)
public class TermsAggregationParser extends BucketAggregationParser {

    @Override
    public TermsAggregation parse(final Aggregate result, final String name) {
        if (result.isSterms()) {
            return parseTerms(result.sterms(), name);
        }
        throw new IllegalArgumentException("Expected StringTermsAggregate, got '%s', with name '%s'".formatted(
                result.getClass().getSimpleName(), name));
    }

    private TermsAggregation parseTerms(final StringTermsAggregate terms, final String name) {
        final var builder = TermsAggregation.builder()
                .name(name)
                .countError(terms.docCountErrorUpperBound())
                .otherDocCount(terms.sumOtherDocCount());

        final Collection<StringTermsBucket> buckets = terms.buckets().isArray()
                ? terms.buckets().array() : terms.buckets().keyed().values();
        buckets.stream()
                .map(this::parseBucket)
                .forEachOrdered(builder::bucket);
        return builder.build();
    }

    private Bucket parseBucket(final StringTermsBucket bucket) {
        final var builder = Bucket.builder()
                .key(bucket.key()._get())
                .count(bucket.docCount());
        applyIfNotNull(bucket.docCountErrorUpperBound(), builder::countError);
        parseSubAggregations(bucket.aggregations())
                .forEach(result -> builder.aggregation(result.getName(), result));
        return builder.build();
    }
}
