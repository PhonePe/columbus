package com.phonepe.columbus.search.misc.bucket;

import com.phonepe.columbus.search.sort.SortOrder;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Data
@Jacksonized
@Builder(toBuilder = true)
public class BucketSortOrder {

    public static final String COUNT = "_count";
    public static final String KEY = "_key";

    private SortOrder order;

    private String name;

    public static BucketSortOrder.BucketSortOrderBuilder count() {
        return builder()
                .name(COUNT)
                .order(SortOrder.DESC);
    }

    public static BucketSortOrder.BucketSortOrderBuilder key() {
        return builder()
                .name(KEY)
                .order(SortOrder.ASC);
    }
}
