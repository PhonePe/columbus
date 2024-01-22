package com.phonepe.commons.columbus.search.query.compound;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.phonepe.commons.columbus.search.query.AbstractQuery;
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
@JsonTypeName(BoolQuery.TYPE)
public class BoolQuery extends AbstractQuery {

    public static final String TYPE = "BOOL";

    @JsonAlias("must")
    @Singular(value = "must", ignoreNullCollections = true)
    private List<@NonNull Query> mustClauses;

    @JsonAlias("should")
    @Singular(value = "should", ignoreNullCollections = true)
    private List<@NonNull Query> shouldClauses;

    @JsonAlias("filter")
    @Singular(value = "filter", ignoreNullCollections = true)
    private List<@NonNull Query> filterClauses;

    @JsonAlias({"mustNot", "must_not"})
    @Singular(value = "mustNot", ignoreNullCollections = true)
    private List<@NonNull Query> mustNotClauses;

    private String minimumMatch;

    @Override
    public @NonNull String getType() {
        return TYPE;
    }
}
