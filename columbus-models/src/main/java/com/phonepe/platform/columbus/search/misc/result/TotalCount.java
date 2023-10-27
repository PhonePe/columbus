package com.phonepe.platform.columbus.search.misc.result;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Builder
public class TotalCount {

    private final long count;

    @Builder.Default
    @Accessors(fluent = true)
    private final boolean exact = true;

    public static TotalCount exact(final long count) {
        return TotalCount.builder()
                .count(count)
                .exact(true)
                .build();
    }

    public static TotalCount atLeast(final long count) {
        return TotalCount.builder()
                .count(count)
                .exact(false)
                .build();
    }
}
