package com.phonepe.commons.columbus.search.retriever;

import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Data;
import lombok.NonNull;
import lombok.Singular;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@Data
@Jacksonized
@SuperBuilder(toBuilder = true)
@JsonTypeName(PinnedRetriever.TYPE)
public class PinnedRetriever implements Retriever {

    public static final String TYPE = "PINNED";

    @NonNull
    @Singular(ignoreNullCollections = true)
    private List<String> ids;

    private Retriever retriever;

    @Override
    public @NonNull String getType() {
        return TYPE;
    }
}
