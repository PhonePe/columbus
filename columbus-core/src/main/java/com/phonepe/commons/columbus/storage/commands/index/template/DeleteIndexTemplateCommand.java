package com.phonepe.commons.columbus.storage.commands.index.template;

import com.phonepe.commons.columbus.command.Command;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

/**
 * A command to delete an index template.
 */
@Data
@Builder
public class DeleteIndexTemplateCommand implements Command<Void> {

    public static final String COMMAND_NAME = "DELETE_INDEX_TEMPLATE";

    @NonNull
    private final String templateName;

    @Override
    public String name() {
        return COMMAND_NAME;
    }
}
