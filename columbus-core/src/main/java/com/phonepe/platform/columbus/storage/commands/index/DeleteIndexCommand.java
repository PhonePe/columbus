package com.phonepe.platform.columbus.storage.commands.index;

import com.phonepe.platform.columbus.command.Command;
import com.phonepe.platform.columbus.dao.index.Index;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

/**
 * A command to delete an index.
 * <p>
 * The result indicates if the operation is successful.
 */
@Data
@Builder
public class DeleteIndexCommand implements Command<Boolean> {

    public static final String COMMAND_NAME = "DELETE_INDEX";

    @NonNull
    private final Index index;

    @Override
    public String name() {
        return COMMAND_NAME;
    }
}
