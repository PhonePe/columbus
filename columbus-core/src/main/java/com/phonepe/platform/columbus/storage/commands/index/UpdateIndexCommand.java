package com.phonepe.platform.columbus.storage.commands.index;

import com.phonepe.platform.columbus.command.Command;
import com.phonepe.platform.columbus.dao.index.Index;
import com.phonepe.platform.columbus.dao.index.IndexProperties;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

/**
 * A command to check if an index exists.
 */
@Data
@Builder
public class UpdateIndexCommand implements Command<Void> {

    public static final String COMMAND_NAME = "UPDATE_INDEX";

    @NonNull
    private final Index index;

    @NonNull
    private final IndexProperties properties;

    @Override
    public String name() {
        return COMMAND_NAME;
    }
}
