package com.phonepe.commons.columbus.search.retriever;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.phonepe.commons.columbus.search.query.Query;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.Singular;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

import java.util.List;
import java.util.Map;

@Data
@Jacksonized
@SuperBuilder(toBuilder = true)
@JsonTypeName(LinearRetriever.TYPE)
public class LinearRetriever implements Retriever {

    public static final String TYPE = "LINEAR";

    @Singular(ignoreNullCollections = true)
    private final Map<@NonNull Retriever, @NonNull Float> retrievers;

    @JsonAlias("filter")
    @Singular(value = "filter", ignoreNullCollections = true)
    private List<@NonNull Query> filterClauses;

    private Integer rankWindowSize;

    @Builder.Default
    private String normalizer = "none";

    @Override
    public @NonNull String getType() {
        return TYPE;
    }
}
