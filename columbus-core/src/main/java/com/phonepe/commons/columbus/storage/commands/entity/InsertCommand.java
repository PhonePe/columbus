package com.phonepe.commons.columbus.storage.commands.entity;

import com.phonepe.commons.columbus.command.Command;
import com.phonepe.commons.columbus.dao.id.Id;
import com.phonepe.commons.columbus.dao.index.Index;
import com.phonepe.commons.columbus.dao.node.NodeId;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;

/**
 * A command to insert an entity into an index.
 */
@Data
@SuperBuilder(toBuilder = true)
public class InsertCommand<T> implements Command<Void> {

    public static final String COMMAND_NAME = "INSERT";

    @NonNull
    private final Index index;

    @NonNull
    private final Id id;

    private final NodeId nodeId;

    private final T value;

    public static <T> InsertCommandBuilder<T, ?, ?> withValue(final T value) {
        return InsertCommand.<T>builder()
                .value(value);
    }

    @Override
    public String name() {
        return COMMAND_NAME;
    }
}
