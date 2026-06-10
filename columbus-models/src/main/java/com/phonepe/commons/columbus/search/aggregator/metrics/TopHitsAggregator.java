package com.phonepe.commons.columbus.search.aggregator.metrics;

import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.phonepe.commons.columbus.search.aggregator.AbstractAggregator;
import com.phonepe.commons.columbus.search.misc.query.Projection;
import com.phonepe.commons.columbus.search.sort.Sort;
import com.phonepe.commons.columbus.type.EntityType;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.Singular;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@JsonClassDescription("Metrics aggregator that returns the top matching documents within each bucket")
@Data
@Jacksonized
@SuperBuilder(toBuilder = true)
@JsonTypeName(TopHitsAggregator.TYPE)
public class TopHitsAggregator<T> extends AbstractAggregator {

    public static final String TYPE = "TOP_HITS";

    @JsonPropertyDescription("The starting offset for pagination (default: 0)")
    @Builder.Default
    private int from = 0;

    @JsonPropertyDescription("Maximum number of top documents to return (default: 3)")
    @Builder.Default
    private int size = 3;

    @JsonPropertyDescription("List of sort criteria to determine the top documents")
    @Singular(ignoreNullCollections = true)
    private List<@NonNull Sort> sorts;

    @JsonPropertyDescription("Field projection to control which fields are returned in the documents")
    private Projection projection;

    @JsonPropertyDescription("Entity type for deserialization of returned documents")
    private EntityType<T> entityType;

    @Override
    public @NonNull String getType() {
        return TYPE;
    }
}
