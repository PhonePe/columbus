package com.phonepe.commons.columbus.search.aggregator;

import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;

@JsonClassDescription("Abstract base class for all aggregator implementations providing the common name property")
@Data
@SuperBuilder(toBuilder = true)
public abstract class AbstractAggregator implements Aggregator {

    @NonNull
    @JsonPropertyDescription("Unique identifier name for this aggregator used to reference it in results")
    private final String name;
}
