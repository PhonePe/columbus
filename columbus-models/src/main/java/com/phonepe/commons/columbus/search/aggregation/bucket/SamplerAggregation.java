package com.phonepe.commons.columbus.search.aggregation.bucket;

import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.phonepe.commons.columbus.search.aggregation.AbstractAggregation;
import com.phonepe.commons.columbus.search.aggregation.Aggregation;
import com.phonepe.commons.columbus.search.aggregator.bucket.SamplerAggregator;
import lombok.Data;
import lombok.NonNull;
import lombok.Singular;
import lombok.experimental.SuperBuilder;

import java.util.Map;

@JsonClassDescription("Result of a sampler aggregation containing sub-aggregations computed on a sample of documents")
@Data
@SuperBuilder(toBuilder = true)
public class SamplerAggregation extends AbstractAggregation {

    @JsonPropertyDescription("Number of documents included in the sample")
    private final long count;

    @JsonPropertyDescription("Map of sub-aggregation results computed on the sampled documents")
    @Singular(ignoreNullCollections = true)
    private final Map<@NonNull String, @NonNull Aggregation> aggregations;

    @Override
    public @NonNull String getType() {
        return SamplerAggregator.TYPE;
    }
}
