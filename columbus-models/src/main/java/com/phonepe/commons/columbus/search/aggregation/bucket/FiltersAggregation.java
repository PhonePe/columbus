package com.phonepe.commons.columbus.search.aggregation.bucket;

import com.phonepe.commons.columbus.search.aggregation.Aggregation;
import com.phonepe.commons.columbus.search.aggregation.BaseAggregation;
import com.phonepe.commons.columbus.search.aggregator.bucket.FiltersAggregator;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.Singular;
import lombok.experimental.SuperBuilder;

import java.util.Map;

@Data
@SuperBuilder(toBuilder = true)
public class FiltersAggregation extends BaseAggregation {

    @Singular(ignoreNullCollections = true)
    private final Map<@NonNull String, @NonNull Bucket> buckets;

    @Override
    public @NonNull String getType() {
        return FiltersAggregator.TYPE;
    }

    @Data
    @Builder
    public static final class Bucket {

        private final long count;

        @Singular(ignoreNullCollections = true)
        private final Map<@NonNull String, @NonNull Aggregation> aggregations;
    }
}
