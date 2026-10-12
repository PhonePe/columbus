package com.phonepe.columbus.storage.commands;

import com.phonepe.columbus.command.Command;
import lombok.Builder;
import lombok.Data;

/**
 * A command to get the version of the storage.
 */
@Data
@Builder
public class VersionCommand implements Command<String> {

    public static final String COMMAND_NAME = "VERSION";

    @Override
    public String name() {
        return COMMAND_NAME;
    }
}
