package com.phonepe.commons.columbus.search.query.special;

import com.fasterxml.jackson.annotation.JsonAlias;
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

    private List<Float> queryVector;

    private Integer numCandidates;

    private Integer k;

    @JsonAlias("filter")
    @Singular(value = "filter", ignoreNullCollections = true)
    private List<@NonNull Query> filterClauses;

    private Float similarity;

    private Float oversample;

    @Override
    public @NonNull String getType() {
        return TYPE;
    }
}
