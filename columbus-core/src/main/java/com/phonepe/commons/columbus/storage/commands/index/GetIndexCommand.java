package com.phonepe.commons.columbus.storage.commands.index;

import com.phonepe.commons.columbus.command.Command;
import com.phonepe.commons.columbus.dao.index.Index;
import com.phonepe.commons.columbus.dao.index.IndexProperties;
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
