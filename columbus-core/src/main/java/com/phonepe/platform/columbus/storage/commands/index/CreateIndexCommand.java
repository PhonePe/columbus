package com.phonepe.platform.columbus.storage.commands.index;

import com.phonepe.platform.columbus.command.Command;
import com.phonepe.platform.columbus.dao.index.Index;
import com.phonepe.platform.columbus.dao.index.IndexProperties;
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
