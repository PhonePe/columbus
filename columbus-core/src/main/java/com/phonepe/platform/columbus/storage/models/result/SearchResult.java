package com.phonepe.platform.columbus.storage.models.result;

import com.phonepe.platform.columbus.search.aggregation.Aggregation;
import com.phonepe.platform.columbus.search.misc.result.Result;
import lombok.Data;
import lombok.NonNull;
import lombok.Singular;
import lombok.experimental.SuperBuilder;

import java.util.Map;

/**
 * The result of {@link com.phonepe.platform.columbus.storage.commands.search.SearchCommand SearchCommand}.
 *
 * @param <T> the type of the entities.
 */
@Data
@SuperBuilder(toBuilder = true)
public class SearchResult<T> {

    private final Result<T> result;

    @Singular(ignoreNullCollections = true)
    private final Map<@NonNull String, @NonNull Aggregation> aggregations;
}
