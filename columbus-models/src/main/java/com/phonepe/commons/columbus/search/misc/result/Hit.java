package com.phonepe.commons.columbus.search.misc.result;

import com.phonepe.commons.columbus.dao.id.Id;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.Singular;

import java.util.List;
import java.util.Map;

@Data
@Builder
public class Hit<T> {

    @NonNull
    private final Id id;

    private final T entity;

    private final float score;

    @Singular(ignoreNullCollections = true)
    private final List<Object> sortValues;

    @Singular(ignoreNullCollections = true)
    private final List<@NonNull String> matchedQueries;

    @Singular(ignoreNullCollections = true)
    private final Map<@NonNull String, @NonNull Result<?>> innerResults;
}
