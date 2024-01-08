package com.phonepe.commons.columbus.storage.commands.search;

import com.phonepe.commons.columbus.command.Command;
import com.phonepe.commons.columbus.dao.index.Index;
import com.phonepe.commons.columbus.dao.node.NodeId;
import com.phonepe.commons.columbus.search.aggregator.Aggregator;
import com.phonepe.commons.columbus.search.misc.query.Collapse;
import com.phonepe.commons.columbus.search.misc.query.Projection;
import com.phonepe.commons.columbus.search.page.PageBounds;
import com.phonepe.commons.columbus.search.query.Query;
import com.phonepe.commons.columbus.search.sort.Sort;
import com.phonepe.commons.columbus.storage.models.result.SearchResult;
import com.phonepe.commons.columbus.type.EntityType;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.Singular;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * A command to search entities in an index.
 */
@Data
@Builder
public class SearchCommand<T> implements Command<SearchResult<T>> {

    public static final String COMMAND_NAME = "SEARCH";

    @Singular(ignoreNullCollections = true)
    private final Map<@NonNull Index, @NonNull Float> indexBoosts;

    private final NodeId nodeId;

    private final Query query;

    @Singular(ignoreNullCollections = true)
    private final List<@NonNull Sort> sorts;

    @Singular(ignoreNullCollections = true)
    private final List<@NonNull Aggregator> aggregators;

    private final Collapse collapse;

    private final Query postFilter;

    private final Projection projection;

    private final PageBounds page;

    @NonNull
    private final EntityType<T> entityType;

    private final boolean consistentScoring;

    public static <T> SearchCommandBuilder<T> forType(final EntityType<T> type) {
        return SearchCommand.<T>builder()
                .entityType(type);
    }

    @Override
    public String name() {
        return COMMAND_NAME;
    }

    public static class SearchCommandBuilder<T> {

        public SearchCommandBuilder<T> index(final @NonNull Index index) {
            return indexBoost(index, 1f);
        }

        public SearchCommandBuilder<T> indices(final Collection<? extends @NonNull Index> indices) {
            if (indices != null) {
                indices.forEach(this::index);
            }
            return this;
        }
    }
}
