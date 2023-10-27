package com.phonepe.platform.columbus.search.aggregation.bucket;

import com.phonepe.platform.columbus.search.aggregation.Aggregation;
import com.phonepe.platform.columbus.search.aggregation.BaseAggregation;
import com.phonepe.platform.columbus.search.aggregator.bucket.TermsAggregator;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.Singular;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.Map;

@Data
@SuperBuilder(toBuilder = true)
public class TermsAggregation extends BaseAggregation {

    @Singular(ignoreNullCollections = true)
    private final List<@NonNull Bucket> buckets;

    private final long countError;
    private final long otherDocCount;

    @Override
    public @NonNull String getType() {
        return TermsAggregator.TYPE;
    }

    @Data
    @Builder
    public static final class Bucket {

        private final Object key;
        private final long count;
        private final long countError;

        @Singular(ignoreNullCollections = true)
        private final Map<@NonNull String, @NonNull Aggregation> aggregations;
    }
}
