package com.phonepe.commons.columbus.storage.commands.entity;

import com.phonepe.commons.columbus.command.Command;
import com.phonepe.commons.columbus.dao.id.Id;
import com.phonepe.commons.columbus.dao.index.Index;
import com.phonepe.commons.columbus.dao.node.NodeId;
import com.phonepe.commons.columbus.storage.models.result.UpsertResult;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

/**
 * A command to upsert an entity into an index.
 */
@Data
@Builder
public class UpsertCommand<T> implements Command<UpsertResult> {

    public static final String COMMAND_NAME = "UPSERT";

    @NonNull
    private final Index index;

    @NonNull
    private final Id id;

    private final NodeId nodeId;

    private final T value;

    public static <T> UpsertCommandBuilder<T> withValue(final T value) {
        return UpsertCommand.<T>builder()
                .value(value);
    }

    @Override
    public String name() {
        return COMMAND_NAME;
    }
}
