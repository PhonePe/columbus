package com.phonepe.commons.columbus.search.query.special;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.phonepe.commons.columbus.search.query.FieldQuery;
import com.phonepe.commons.columbus.search.query.Query;
import lombok.Data;
import lombok.NonNull;
import lombok.Singular;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@Data
@Jacksonized
@SuperBuilder(toBuilder = true)
@JsonTypeName(KnnQuery.TYPE)
public class KnnQuery extends FieldQuery {

    public static final String TYPE = "KNN";

    @JsonPropertyDescription("Query vector used for nearest neighbor search")
    private List<Float> queryVector;

    @JsonPropertyDescription("Number of candidate vectors to consider for nearest neighbor search")
    private Integer numCandidates;

    @JsonPropertyDescription("Number of nearest neighbors to retrieve")
    private Integer k;

    @JsonPropertyDescription("Optional list of filter clauses to apply before performing the KNN search")
    @JsonAlias("filter")
    @Singular(value = "filter", ignoreNullCollections = true)
    private List<@NonNull Query> filterClauses;

    @JsonPropertyDescription("Similarity threshold for considering vectors as neighbors")
    private Float similarity;

    @JsonPropertyDescription("Oversampling factor to improve search quality")
    private Float oversample;

    @Override
    public @NonNull String getType() {
        return TYPE;
    }
}
