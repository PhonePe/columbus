package com.phonepe.commons.columbus.search.aggregator.bucket;

import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@JsonClassDescription("Bucket aggregator that limits document processing to a sample, improving performance for expensive sub-aggregations")
@Data
@Jacksonized
@SuperBuilder(toBuilder = true)
@JsonTypeName(SamplerAggregator.TYPE)
public class SamplerAggregator extends BucketAggregator {

    public static final String TYPE = "SAMPLER";

    @JsonPropertyDescription("Maximum number of documents to sample per shard")
    private Integer shardSize;

    @JsonPropertyDescription("Field to use for ensuring diverse sampling across different values")
    private String diversifyField;

    @JsonPropertyDescription("Maximum number of documents to collect per diversification value")
    private Integer maxDuplicates;

    @Override
    public @NonNull String getType() {
        return TYPE;
    }
}
