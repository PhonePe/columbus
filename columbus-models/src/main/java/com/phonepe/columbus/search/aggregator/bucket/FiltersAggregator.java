package com.phonepe.columbus.search.aggregator.bucket;

import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.phonepe.columbus.search.query.Query;
import lombok.Data;
import lombok.NonNull;
import lombok.Singular;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

import java.util.Map;

@JsonClassDescription("Bucket aggregator that creates multiple named buckets, each defined by a filter query")
@Data
@Jacksonized
@SuperBuilder(toBuilder = true)
@JsonTypeName(FiltersAggregator.TYPE)
public class FiltersAggregator extends BucketAggregator {

    public static final String TYPE = "FILTERS";

    @JsonPropertyDescription("Map of bucket names to their corresponding filter queries")
    @Singular(ignoreNullCollections = true)
    private Map<@NonNull String, @NonNull Query> filters;

    @JsonPropertyDescription("Name for an optional bucket that collects all documents not matching any filter")
    private String otherBucketKey;

    @Override
    public @NonNull String getType() {
        return TYPE;
    }
}
