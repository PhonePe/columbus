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
