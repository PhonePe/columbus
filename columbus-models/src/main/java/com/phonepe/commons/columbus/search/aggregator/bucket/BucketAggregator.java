package com.phonepe.commons.columbus.search.aggregator.bucket;

import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.phonepe.commons.columbus.search.aggregator.AbstractAggregator;
import com.phonepe.commons.columbus.search.aggregator.Aggregator;
import lombok.Data;
import lombok.NonNull;
import lombok.Singular;
import lombok.experimental.SuperBuilder;

import java.util.List;

@JsonClassDescription("Abstract base class for bucket aggregators that group documents and support nested sub-aggregations")
@Data
@SuperBuilder(toBuilder = true)
public abstract class BucketAggregator extends AbstractAggregator {

    @JsonPropertyDescription("List of nested aggregators to execute within each bucket")
    @Singular(ignoreNullCollections = true)
    private List<@NonNull Aggregator> subAggregators;
}
