package com.phonepe.commons.columbus.search.aggregation;

import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;

@JsonClassDescription("Abstract base class for all aggregation result implementations providing the common name property")
@Data
@SuperBuilder(toBuilder = true)
public abstract class AbstractAggregation implements Aggregation {

    @NonNull
    @JsonPropertyDescription("Name of this aggregation result, matching the aggregator that produced it")
    private final String name;
}
