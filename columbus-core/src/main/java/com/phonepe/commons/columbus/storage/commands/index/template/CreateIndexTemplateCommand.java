package com.phonepe.commons.columbus.storage.commands.index.template;

import com.phonepe.commons.columbus.command.Command;
import com.phonepe.commons.columbus.dao.index.IndexProperties;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.Singular;

import java.util.List;

/**
 * A command to create a new index template.
 */
@Data
@Builder
public class CreateIndexTemplateCommand implements Command<Void> {

    public static final String COMMAND_NAME = "CREATE_INDEX_TEMPLATE";

    @NonNull
    private final String templateName;

    @Singular(ignoreNullCollections = true)
    private final List<@NonNull String> indexPatterns;

    @NonNull
    private final IndexProperties properties;

    @Override
    public String name() {
        return COMMAND_NAME;
    }
}
