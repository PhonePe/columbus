package com.phonepe.platform.columbus.search.misc.result;

import lombok.Builder;
import lombok.Data;
import lombok.Singular;

import java.util.List;

@Data
@Builder
public class Result<T> {

    @Singular(ignoreNullCollections = true)
    private final List<Hit<T>> hits;

    private final TotalCount totalCount;
    private final Float maxScore;
}
