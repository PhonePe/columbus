package com.phonepe.columbus.search.aggregator.bucket;

import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.phonepe.columbus.search.query.Query;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@JsonClassDescription("Bucket aggregator that creates a single bucket containing documents matching a filter query")
@Data
@Jacksonized
@SuperBuilder(toBuilder = true)
@JsonTypeName(FilterAggregator.TYPE)
public class FilterAggregator extends BucketAggregator {

    public static final String TYPE = "FILTER";

    @JsonPropertyDescription("The filter query that documents must match to be included in the bucket")
    private Query filter;

    @Override
    public @NonNull String getType() {
        return TYPE;
    }
}
