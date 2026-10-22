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
import co.elastic.clients.elasticsearch._types.aggregations.SamplerAggregate;
import com.phonepe.columbus.search.aggregation.bucket.SamplerAggregation;
import lombok.experimental.SuperBuilder;

@SuperBuilder(toBuilder = true)
public class SamplerAggregationParser extends BucketAggregationParser {

    @Override
    public SamplerAggregation parse(final Aggregate result, final String name) {
        if (result.isSampler()) {
            return parseSampler(result.sampler(), name);
        }
        throw new IllegalArgumentException("Expected SamplerAggregate, got '%s', with name '%s'".formatted(
                result.getClass().getSimpleName(), name));
    }

    private SamplerAggregation parseSampler(final SamplerAggregate sampler, final String name) {
        final var builder = SamplerAggregation.builder()
                .name(name)
                .count(sampler.docCount());
        parseSubAggregations(sampler.aggregations())
                .forEach(result -> builder.aggregation(result.getName(), result));
        return builder.build();
    }
}
