package com.phonepe.columbus.storage.commands.retriever;

import com.phonepe.columbus.dao.index.Index;
import com.phonepe.columbus.search.retriever.Retriever;
import com.phonepe.columbus.storage.commands.search.SearchBase;
import com.phonepe.columbus.type.EntityType;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;

import java.util.Collection;

@Data
@SuperBuilder(toBuilder = true)
public class RetrieverCommand<T> extends SearchBase<T> {

    public static final String COMMAND_NAME = "RETRIEVER";

    private final Retriever retriever;

    @SuppressWarnings({"java:S1452", "unchecked"})
    public static <T> RetrieverCommandBuilder<T, ?, ?> forType(final EntityType<T> type) {
        return ((RetrieverCommandBuilder<T, ?, ?>) RetrieverCommand.<T>builder())
                .entityType(type);
    }

    @Override
    public String name() {
        return COMMAND_NAME;
    }

    public abstract static class RetrieverCommandBuilder<T, C extends RetrieverCommand<T>,
            B extends RetrieverCommandBuilder<T, C, B>> extends SearchBaseBuilder<T, C, B> {

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
