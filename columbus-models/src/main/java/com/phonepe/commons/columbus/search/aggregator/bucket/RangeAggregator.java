package com.phonepe.commons.columbus.search.aggregator.bucket;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.phonepe.commons.columbus.search.misc.bucket.NamedRange;
import lombok.Data;
import lombok.NonNull;
import lombok.Singular;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@Data
@Jacksonized
@SuperBuilder(toBuilder = true)
@JsonTypeName(RangeAggregator.TYPE)
public class RangeAggregator extends BucketAggregator {

    public static final String TYPE = "RANGE";

    private String field;

    @Singular(ignoreNullCollections = true)
    private List<@NonNull NamedRange> ranges;

    @Override
    public @NonNull String getType() {
        return TYPE;
    }
}
