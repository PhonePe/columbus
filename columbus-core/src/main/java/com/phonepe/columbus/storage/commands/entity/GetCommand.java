package com.phonepe.columbus.storage.commands.entity;

import com.phonepe.columbus.command.Command;
import com.phonepe.columbus.dao.id.Id;
import com.phonepe.columbus.dao.index.Index;
import com.phonepe.columbus.dao.node.NodeId;
import com.phonepe.columbus.search.misc.query.Projection;
import com.phonepe.columbus.type.EntityType;
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
