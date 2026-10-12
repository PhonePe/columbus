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
