package com.phonepe.commons.columbus.search.aggregation;

import lombok.Data;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder(toBuilder = true)
public abstract class AbstractAggregation implements Aggregation {

    @NonNull
    private final String name;
}
