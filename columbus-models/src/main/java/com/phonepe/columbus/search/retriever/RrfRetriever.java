package com.phonepe.columbus.search.retriever;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonClassDescription;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.phonepe.columbus.search.query.Query;
import lombok.Data;
import lombok.NonNull;
import lombok.Singular;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@JsonClassDescription("Retriever that combines results from multiple retrievers using Reciprocal Rank Fusion (RRF) technique.")
@Data
@Jacksonized
@SuperBuilder(toBuilder = true)
@JsonTypeName(RrfRetriever.TYPE)
public class RrfRetriever implements Retriever {

    public static final String TYPE = "RRF";

    @JsonPropertyDescription("List of retrievers whose results will be combined using Reciprocal Rank Fusion")
    @Singular(value = "retriever", ignoreNullCollections = true)
    private List<Retriever> retrievers;

    @JsonPropertyDescription("List of filter queries to be applied to the combined results")
    @JsonAlias("filter")
    @Singular(value = "filter", ignoreNullCollections = true)
    private List<@NonNull Query> filterClauses;

    @JsonPropertyDescription("Constant value used in the RRF scoring formula")
    private Integer rankConstant;

    @JsonPropertyDescription("Window size for considering top results from each retriever")
    private Integer rankWindowSize;

    @Override
    public @NonNull String getType() {
        return TYPE;
    }
}
