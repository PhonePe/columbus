package com.phonepe.commons.columbus.search.retriever;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.phonepe.commons.columbus.search.misc.query.Collapse;
import com.phonepe.commons.columbus.search.query.Query;
import com.phonepe.commons.columbus.search.sort.Sort;
import lombok.Data;
import lombok.NonNull;
import lombok.Singular;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@Data
@Jacksonized
@SuperBuilder(toBuilder = true)
@JsonTypeName(StandardRetriever.TYPE)
public class StandardRetriever implements Retriever {

    public static final String TYPE = "STANDARD";

    @JsonPropertyDescription("Main query to retrieve documents matching the criteria")
    private Query query;

    @JsonAlias("filter")
    @Singular(value = "filter", ignoreNullCollections = true)
    @JsonPropertyDescription("List of filter clauses to further refine the search results")
    private List<@NonNull Query> filterClauses;

    @JsonPropertyDescription("Sorting criteria for ordering the search results")
    private Sort sort;

    @JsonPropertyDescription("Minimum score threshold for documents to be included in the results")
    private Float minScore;

    @JsonPropertyDescription("Collapse configuration to group search results based on field values")
    private Collapse collapse;

    @Override
    public @NonNull String getType() {
        return TYPE;
    }
}
