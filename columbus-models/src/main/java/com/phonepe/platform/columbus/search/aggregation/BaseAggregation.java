package com.phonepe.platform.columbus.search.aggregation;

import lombok.Data;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder(toBuilder = true)
public abstract class BaseAggregation implements Aggregation {

    @NonNull
    private final String name;
}
