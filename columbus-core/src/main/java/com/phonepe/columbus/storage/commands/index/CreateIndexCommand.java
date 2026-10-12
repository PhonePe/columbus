package com.phonepe.columbus.storage.commands.index;

import com.phonepe.columbus.command.Command;
import com.phonepe.columbus.dao.index.Index;
import com.phonepe.columbus.dao.index.IndexProperties;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

/**
 * A command to create a new index.
 */
@Data
@Builder
public class CreateIndexCommand implements Command<Void> {

    public static final String COMMAND_NAME = "CREATE_INDEX";

    @NonNull
    private final Index index;

    @NonNull
    private final IndexProperties properties;

    @Override
    public String name() {
        return COMMAND_NAME;
    }
}
