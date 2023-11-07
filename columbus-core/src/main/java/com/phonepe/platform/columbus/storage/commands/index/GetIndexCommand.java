package com.phonepe.platform.columbus.storage.commands.index;

import com.phonepe.platform.columbus.command.Command;
import com.phonepe.platform.columbus.dao.index.Index;
import com.phonepe.platform.columbus.dao.index.IndexProperties;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

/**
 * A command to get the properties of an index.
 */
@Data
@Builder
public class GetIndexCommand implements Command<IndexProperties> {

    public static final String COMMAND_NAME = "GET_INDEX";

    @NonNull
    private final Index index;

    @Override
    public String name() {
        return COMMAND_NAME;
    }
}
