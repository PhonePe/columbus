package com.phonepe.columbus.storage.commands.search;

import com.phonepe.columbus.command.Command;
import com.phonepe.columbus.dao.index.Index;
import com.phonepe.columbus.dao.node.NodeId;
import com.phonepe.columbus.search.aggregator.Aggregator;
import com.phonepe.columbus.search.misc.query.Collapse;
import com.phonepe.columbus.search.misc.query.Projection;
import com.phonepe.columbus.search.page.PageBounds;
import com.phonepe.columbus.search.query.Query;
import com.phonepe.columbus.storage.models.result.SearchResult;
import com.phonepe.columbus.type.EntityType;
import lombok.Data;
import lombok.NonNull;
import lombok.Singular;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.Map;

@Data
@SuperBuilder(toBuilder = true)
public abstract class SearchBase<T> implements Command<SearchResult<T>> {

    @Singular(ignoreNullCollections = true)
    private final Map<@NonNull Index, @NonNull Float> indexBoosts;

    private final NodeId nodeId;

    private final Query postFilter;

    private final PageBounds page;

    @Singular(ignoreNullCollections = true)
    private final List<@NonNull Aggregator> aggregators;

    private final Collapse collapse;

    private final Projection projection;

    @NonNull
    private final EntityType<T> entityType;
}
