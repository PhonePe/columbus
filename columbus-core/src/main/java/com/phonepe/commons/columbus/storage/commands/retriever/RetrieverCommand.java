package com.phonepe.commons.columbus.storage.commands.retriever;

import com.phonepe.commons.columbus.command.Command;
import com.phonepe.commons.columbus.dao.index.Index;
import com.phonepe.commons.columbus.dao.node.NodeId;
import com.phonepe.commons.columbus.search.aggregator.Aggregator;
import com.phonepe.commons.columbus.search.misc.query.Collapse;
import com.phonepe.commons.columbus.search.misc.query.Projection;
import com.phonepe.commons.columbus.search.page.PageBounds;
import com.phonepe.commons.columbus.search.query.Query;
import com.phonepe.commons.columbus.search.retriever.Retriever;
import com.phonepe.commons.columbus.storage.models.result.SearchResult;
import com.phonepe.commons.columbus.type.EntityType;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.Singular;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@Data
@Builder
public class RetrieverCommand<T> implements Command<SearchResult<T>> {

    public static final String COMMAND_NAME = "RETRIEVER";

    @Singular(ignoreNullCollections = true)
    private final Map<@NonNull Index, @NonNull Float> indexBoosts;

    private final NodeId nodeId;

    private final Retriever retriever;

    @Singular(ignoreNullCollections = true)
    private final List<@NonNull Aggregator> aggregators;

    private final Collapse collapse;

    private final Query postFilter;

    private final Projection projection;

    private final PageBounds page;

    @NonNull
    private final EntityType<T> entityType;

    public static <T> RetrieverCommandBuilder<T> forType(final EntityType<T> type) {
        return RetrieverCommand.<T>builder()
                .entityType(type);
    }

    @Override
    public String name() {
        return COMMAND_NAME;
    }

    public static class RetrieverCommandBuilder<T> {

        public RetrieverCommand.RetrieverCommandBuilder<T> index(final @NonNull Index index) {
            return indexBoost(index, 1f);
        }

        public RetrieverCommand.RetrieverCommandBuilder<T> indices(final Collection<? extends @NonNull Index> indices) {
            if (indices != null) {
                indices.forEach(this::index);
            }
            return this;
        }
    }
}
