package com.phonepe.columbus.storage.commands.entity;

import com.phonepe.columbus.command.Command;
import com.phonepe.columbus.dao.id.Id;
import com.phonepe.columbus.dao.index.Index;
import com.phonepe.columbus.dao.node.NodeId;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

/**
 * A command to delete an entity from an index.
 */
@Data
@Builder
public class DeleteCommand implements Command<Void> {

    public static final String COMMAND_NAME = "DELETE";

    @NonNull
    private final Index index;

    @NonNull
    private final Id id;

    private final NodeId nodeId;

    @Override
    public String name() {
        return COMMAND_NAME;
    }
}
