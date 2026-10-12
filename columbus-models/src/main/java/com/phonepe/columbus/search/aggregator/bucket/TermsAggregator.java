package com.phonepe.columbus.search.aggregator.bucket;

import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.phonepe.columbus.search.misc.bucket.BucketSortOrder;
import com.phonepe.columbus.search.misc.bucket.IncludeExclude;
import lombok.Data;
import lombok.NonNull;
import lombok.Singular;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@JsonClassDescription("Bucket aggregator that creates buckets for each unique term value in a field")
@Data
@Jacksonized
@SuperBuilder(toBuilder = true)
@JsonTypeName(TermsAggregator.TYPE)
public class TermsAggregator extends BucketAggregator {

    public static final String TYPE = "TERMS";

    @JsonPropertyDescription("The field to extract terms from")
    private String field;

    @JsonPropertyDescription("Maximum number of term buckets to return")
    private Integer size;

    @JsonPropertyDescription("Number of candidate terms to collect from each shard (should be >= size)")
    private Integer shardSize;

    @JsonPropertyDescription("Configuration to include or exclude specific terms based on patterns or exact values")
    private IncludeExclude includeExclude;

    @JsonPropertyDescription("List of sort orders to determine which top terms are returned")
    @Singular(ignoreNullCollections = true)
    private List<@NonNull BucketSortOrder> orders;

    @JsonPropertyDescription("Minimum number of documents a term must have to be included in results")
    private Integer minDocCount;

    @JsonPropertyDescription("Minimum number of documents a term must have on a shard to be considered")
    private Integer shardMinDocCount;

    @JsonPropertyDescription("Default value to use for documents where the field is missing")
    private Object missingValue;

    @JsonPropertyDescription("Collection mode strategy (breadth_first or depth_first)")
    private String collectMode;

    @JsonPropertyDescription("Whether to show the document count error for each bucket")
    private Boolean showCountError;

    @Override
    public @NonNull String getType() {
        return TYPE;
    }
}
