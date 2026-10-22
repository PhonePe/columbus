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

import com.phonepe.columbus.search.aggregation.bucket.TermsAggregation;
import com.phonepe.columbus.search.aggregation.bucket.TermsAggregation.Bucket;
import lombok.experimental.SuperBuilder;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;

@SuperBuilder(toBuilder = true)
public class TermsAggregationParser extends BucketAggregationParser {

    @Override
    public TermsAggregation parse(final Aggregation result) {
        if (result instanceof ParsedTerms terms) {
            return parseTerms(terms);
        }
        throw new IllegalArgumentException("Expected ParsedTerms, got '%s', with name '%s'".formatted(
                result.getClass().getSimpleName(), result.getName()));
    }

    private TermsAggregation parseTerms(final ParsedTerms terms) {
        final var builder = TermsAggregation.builder()
                .name(terms.getName())
                .countError(terms.getDocCountError())
                .otherDocCount(terms.getSumOfOtherDocCounts());
        terms.getBuckets().stream()
                .map(this::parseBucket)
                .forEachOrdered(builder::bucket);
        return builder.build();
    }

    private Bucket parseBucket(final Terms.Bucket bucket) {
        final var builder = Bucket.builder()
                .key(bucket.getKey())
                .count(bucket.getDocCount())
                .countError(bucket.getDocCountError());
        parseSubAggregations(bucket.getAggregations().asList())
                .forEach(result -> builder.aggregation(result.getName(), result));
        return builder.build();
    }
}
