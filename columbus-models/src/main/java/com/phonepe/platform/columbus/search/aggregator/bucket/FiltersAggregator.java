package com.phonepe.platform.columbus.search.aggregator.bucket;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.phonepe.platform.columbus.search.query.Query;
import lombok.Data;
import lombok.NonNull;
import lombok.Singular;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

import java.util.Map;

@Data
@Jacksonized
@SuperBuilder(toBuilder = true)
@JsonTypeName(FiltersAggregator.TYPE)
public class FiltersAggregator extends BucketAggregator {

    public static final String TYPE = "FILTERS";

    @Singular(ignoreNullCollections = true)
    private Map<@NonNull String, @NonNull Query> filters;

    private String otherBucketKey;

    @Override
    public @NonNull String getType() {
        return TYPE;
    }
}
