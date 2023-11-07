package com.phonepe.platform.columbus.storage.commands.entity;

import com.phonepe.platform.columbus.command.Command;
import com.phonepe.platform.columbus.dao.id.Id;
import com.phonepe.platform.columbus.dao.index.Index;
import com.phonepe.platform.columbus.dao.node.NodeId;
import com.phonepe.platform.columbus.search.misc.query.Projection;
import com.phonepe.platform.columbus.type.EntityType;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import java.util.Optional;

/**
 * A command to get an entity from an index.
 */
@Data
@Builder
public class GetCommand<T> implements Command<Optional<T>> {

    public static final String COMMAND_NAME = "GET";

    @NonNull
    private final Index index;

    @NonNull
    private final Id id;

    private final NodeId nodeId;

    private final Projection projection;

    @NonNull
    private final EntityType<T> entityType;

    public static <T> GetCommandBuilder<T> forType(final EntityType<T> type) {
        return GetCommand.<T>builder()
                .entityType(type);
    }

    @Override
    public String name() {
        return COMMAND_NAME;
    }
}
