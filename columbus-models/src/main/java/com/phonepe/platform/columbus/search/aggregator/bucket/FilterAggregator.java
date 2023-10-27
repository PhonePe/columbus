package com.phonepe.platform.columbus.search.aggregator.bucket;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.phonepe.platform.columbus.search.query.Query;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@Data
@Jacksonized
@SuperBuilder(toBuilder = true)
@JsonTypeName(FilterAggregator.TYPE)
public class FilterAggregator extends BucketAggregator {

    public static final String TYPE = "FILTER";

    private Query filter;

    @Override
    public @NonNull String getType() {
        return TYPE;
    }
}
