package com.phonepe.columbus.search.aggregator.bucket;

import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.phonepe.columbus.search.misc.bucket.NamedRange;
import lombok.Data;
import lombok.NonNull;
import lombok.Singular;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@JsonClassDescription("Bucket aggregator that groups documents into named buckets based on numeric or date ranges")
@Data
@Jacksonized
@SuperBuilder(toBuilder = true)
@JsonTypeName(RangeAggregator.TYPE)
public class RangeAggregator extends BucketAggregator {

    public static final String TYPE = "RANGE";

    @JsonPropertyDescription("The field to use for range bucketing")
    private String field;

    @JsonPropertyDescription("List of named ranges defining the buckets")
    @Singular(ignoreNullCollections = true)
    private List<@NonNull NamedRange> ranges;

    @Override
    public @NonNull String getType() {
        return TYPE;
    }
}
