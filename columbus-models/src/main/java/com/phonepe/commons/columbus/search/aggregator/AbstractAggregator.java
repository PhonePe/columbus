package com.phonepe.commons.columbus.search.aggregator;

import lombok.Data;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder(toBuilder = true)
public abstract class AbstractAggregator implements Aggregator {

    @NonNull
    private final String name;
}
