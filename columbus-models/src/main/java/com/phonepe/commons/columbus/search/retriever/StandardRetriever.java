package com.phonepe.commons.columbus.search.retriever;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.phonepe.commons.columbus.search.filter.Filter;
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

    private Query query;

    @JsonAlias("filter")
    @Singular(value = "filter", ignoreNullCollections = true)
    private List<@NonNull Query> filterClauses;

    private Sort sort;

    private Float minScore;

    private Collapse collapse;

    @Override
    public @NonNull String getType() {
        return TYPE;
    }
}
