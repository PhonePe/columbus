package com.phonepe.commons.columbus.search.misc.query;

import com.phonepe.commons.columbus.search.sort.Sort;
import com.phonepe.commons.columbus.type.EntityType;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.Singular;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

/**
 * Represents a query in a collapsed group.
 *
 * @param <T> the type of the result entities.
 */
@Data
@Builder
@Jacksonized
public class InnerQuery<T> {

    @NonNull
    private final String name;

    @Builder.Default
    private int from = 0;
    @Builder.Default
    private int size = 3;

    @Singular(ignoreNullCollections = true)
    private List<@NonNull Sort> sorts;

    private Projection projection;
    private Collapse innerCollapse;

    @NonNull
    private EntityType<T> entityType;

    private Boolean ignoreInvalid;

    public static <T> InnerQueryBuilder<T> forType(final EntityType<T> type) {
        return InnerQuery.<T>builder()
                .entityType(type);
    }
}
