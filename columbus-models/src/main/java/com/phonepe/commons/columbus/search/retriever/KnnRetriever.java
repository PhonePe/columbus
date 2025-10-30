package com.phonepe.commons.columbus.search.retriever;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.phonepe.commons.columbus.search.query.special.KnnQuery;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@Data
@Jacksonized
@SuperBuilder(toBuilder = true)
@JsonTypeName(KnnRetriever.TYPE)
public class KnnRetriever extends KnnQuery implements Retriever {

    public static final String TYPE = "KNN";

    @Override
    public @NonNull String getType() {
        return TYPE;
    }
}
