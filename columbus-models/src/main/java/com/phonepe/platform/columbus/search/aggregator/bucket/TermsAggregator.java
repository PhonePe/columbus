package com.phonepe.platform.columbus.search.aggregator.bucket;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.phonepe.platform.columbus.search.misc.bucket.BucketSortOrder;
import com.phonepe.platform.columbus.search.misc.bucket.IncludeExclude;
import lombok.Data;
import lombok.NonNull;
import lombok.Singular;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@Data
@Jacksonized
@SuperBuilder(toBuilder = true)
@JsonTypeName(TermsAggregator.TYPE)
public class TermsAggregator extends BucketAggregator {

    public static final String TYPE = "TERMS";

    private String field;

    private Integer size;
    private Integer shardSize;

    private IncludeExclude includeExclude;

    @Singular(ignoreNullCollections = true)
    private List<@NonNull BucketSortOrder> orders;

    private Integer minDocCount;
    private Integer shardMinDocCount;

    private Object missingValue;
    private String collectMode;
    private Boolean showCountError;

    @Override
    public @NonNull String getType() {
        return TYPE;
    }
}
