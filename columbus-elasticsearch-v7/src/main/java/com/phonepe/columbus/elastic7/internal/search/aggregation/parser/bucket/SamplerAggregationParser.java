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

import com.phonepe.columbus.search.aggregation.bucket.SamplerAggregation;
import lombok.experimental.SuperBuilder;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.bucket.sampler.ParsedSampler;

@SuperBuilder(toBuilder = true)
public class SamplerAggregationParser extends BucketAggregationParser {

    @Override
    public SamplerAggregation parse(final Aggregation result) {
        if (result instanceof ParsedSampler sampler) {
            return parseSampler(sampler);
        }
        throw new IllegalArgumentException("Expected ParsedSampler, got '%s', with name '%s'".formatted(
                result.getClass().getSimpleName(), result.getName()));
    }

    private SamplerAggregation parseSampler(final ParsedSampler sampler) {
        final var builder = SamplerAggregation.builder()
                .name(sampler.getName())
                .count(sampler.getDocCount());
        parseSubAggregations(sampler.getAggregations().asList())
                .forEach(result -> builder.aggregation(result.getName(), result));
        return builder.build();
    }
}
