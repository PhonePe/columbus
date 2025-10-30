package com.phonepe.commons.columbus.search.retriever;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonTypeName;
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
@JsonTypeName(RrfRetriever.TYPE)
public class RrfRetriever implements Retriever {

    public static final String TYPE = "RRF";

    @Singular(value = "retriever", ignoreNullCollections = true)
    private List<Retriever> retrievers;

    @JsonAlias("filter")
    @Singular(value = "filter", ignoreNullCollections = true)
    private List<@NonNull Query> filterClauses;

    private int rankConstant;

    private int rankWindowSize;

    @Override
    public @NonNull String getType() {
        return TYPE;
    }
}
