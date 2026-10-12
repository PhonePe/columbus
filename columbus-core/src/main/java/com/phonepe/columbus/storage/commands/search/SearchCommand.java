package com.phonepe.columbus.storage.commands.search;

import com.phonepe.columbus.dao.index.Index;
import com.phonepe.columbus.search.query.Query;
import com.phonepe.columbus.search.sort.Sort;
import com.phonepe.columbus.type.EntityType;
import lombok.Data;
import lombok.NonNull;
import lombok.Singular;
import lombok.experimental.SuperBuilder;

import java.util.Collection;
import java.util.List;

/**
 * A command to search entities in an index.
 */
@Data
@SuperBuilder(toBuilder = true)
public class SearchCommand<T> extends SearchBase<T> {

    public static final String COMMAND_NAME = "SEARCH";

    private final Query query;

    @Singular(ignoreNullCollections = true)
    private final List<@NonNull Sort> sorts;

    private final boolean consistentScoring;

    @SuppressWarnings({"java:S1452", "unchecked"})
    public static <T> SearchCommandBuilder<T, ?, ?> forType(final EntityType<T> type) {
        return ((SearchCommandBuilder<T, ?, ?>) SearchCommand.<T>builder())
                .entityType(type);
    }

    @Override
    public String name() {
        return COMMAND_NAME;
    }

    public abstract static class SearchCommandBuilder<T, C extends SearchCommand<T>,
            B extends SearchCommand.SearchCommandBuilder<T, C, B>> extends SearchBaseBuilder<T, C, B> {

        public B index(final @NonNull Index index) {
            return indexBoost(index, 1f);
        }

        public B indices(final Collection<? extends @NonNull Index> indices) {
            if (indices != null) {
                indices.forEach(this::index);
            }
            return self();
        }
    }
}
