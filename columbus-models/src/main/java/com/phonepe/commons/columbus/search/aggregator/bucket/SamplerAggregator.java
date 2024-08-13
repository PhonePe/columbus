package com.phonepe.commons.columbus.search.aggregator.bucket;

import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@Data
@Jacksonized
@SuperBuilder(toBuilder = true)
@JsonTypeName(SamplerAggregator.TYPE)
public class SamplerAggregator extends BucketAggregator {

    public static final String TYPE = "SAMPLER";

    private Integer shardSize;

    private String diversifyField;
    private Integer maxDuplicates;

    @Override
    public @NonNull String getType() {
        return TYPE;
    }
}
