package com.phonepe.platform.columbus.search.aggregator;

import lombok.Data;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder(toBuilder = true)
public abstract class BaseAggregator implements Aggregator {

    @NonNull
    private final String name;
}
