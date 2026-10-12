package com.phonepe.columbus.storage.commands.search;

import com.phonepe.columbus.command.Command;
import com.phonepe.columbus.dao.index.Index;
import com.phonepe.columbus.dao.node.NodeId;
import com.phonepe.columbus.search.misc.result.TotalCount;
import com.phonepe.columbus.search.query.Query;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.Singular;

import java.util.List;

/**
 * A command to count entities matching a query in an index.
 * <p>
 * When an approximate count is acceptable beyond a certain limit, {@link #threshold} can be specified.
 */
@Data
@Builder
public class CountCommand implements Command<TotalCount> {

    public static final String COMMAND_NAME = "COUNT";

    @Singular(ignoreNullCollections = true)
    private final List<@NonNull Index> indices;

    private final NodeId nodeId;

    private final Query query;

    private final Long threshold;

    @Override
    public String name() {
        return COMMAND_NAME;
    }
}
