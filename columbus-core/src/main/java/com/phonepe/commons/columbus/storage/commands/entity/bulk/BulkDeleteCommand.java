package com.phonepe.commons.columbus.storage.commands.entity.bulk;

import com.phonepe.commons.columbus.command.Command;
import com.phonepe.commons.columbus.dao.id.Id;
import com.phonepe.commons.columbus.dao.index.Index;
import com.phonepe.commons.columbus.dao.misc.OperationFailure;
import com.phonepe.commons.columbus.storage.models.RequestExecutionPolicy;
import com.phonepe.commons.columbus.storage.models.entity.EntityId;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.Singular;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * A command to delete a batch of entities from an index.
 */
@Data
@Builder
public class BulkDeleteCommand implements Command<BulkDeleteCommand.Result> {

    public static final String COMMAND_NAME = "BULK_DELETE";

    @NonNull
    private final Index index;

    @Singular(ignoreNullCollections = true)
    private final List<@NonNull EntityId> ids;

    private final RequestExecutionPolicy executionPolicy;

    @Override
    public String name() {
        return COMMAND_NAME;
    }

    @Data
    @Builder
    public static class Result {

        @Singular(value = "notFound", ignoreNullCollections = true)
        private final Set<Id> notFound;

        @Singular(value = "failure", ignoreNullCollections = true)
        private final Map<Id, OperationFailure> failure;

        public boolean hasFailures() {
            return !failure.isEmpty() || !notFound.isEmpty();
        }
    }
}
