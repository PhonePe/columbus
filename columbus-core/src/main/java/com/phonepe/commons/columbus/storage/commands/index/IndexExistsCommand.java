package com.phonepe.commons.columbus.storage.commands.index;

import com.phonepe.commons.columbus.command.Command;
import com.phonepe.commons.columbus.dao.index.Index;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

/**
 * A command to check if an index exists.
 */
@Data
@Builder
public class IndexExistsCommand implements Command<Boolean> {

    public static final String COMMAND_NAME = "INDEX_EXISTS";

    @NonNull
    private final Index index;

    @Override
    public String name() {
        return COMMAND_NAME;
    }
}
