package com.phonepe.commons.columbus.storage.commands.entity.bulk;

import com.phonepe.commons.columbus.command.Command;
import com.phonepe.commons.columbus.dao.id.Id;
import com.phonepe.commons.columbus.dao.index.Index;
import com.phonepe.commons.columbus.dao.misc.OperationFailure;
import com.phonepe.commons.columbus.storage.models.RequestExecutionPolicy;
import com.phonepe.commons.columbus.storage.models.entity.Entity;
import com.phonepe.commons.columbus.storage.models.result.UpsertResult;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.Singular;

import java.util.List;
import java.util.Map;

/**
 * A command to upsert a batch of entities into an index.
 */
@Data
@Builder
public class BulkUpsertCommand<T> implements Command<BulkUpsertCommand.Result> {

    public static final String COMMAND_NAME = "BULK_UPSERT";

    @NonNull
    private final Index index;

    @Singular(ignoreNullCollections = true)
    private final List<@NonNull Entity<T>> entities;

    private final RequestExecutionPolicy executionPolicy;

    @Override
    public String name() {
        return COMMAND_NAME;
    }

    @Data
    @Builder
    public static class Result {

        @Singular(value = "success", ignoreNullCollections = true)
        private final Map<Id, UpsertResult> success;

        @Singular(value = "failure", ignoreNullCollections = true)
        private final Map<Id, OperationFailure> failure;

        public boolean hasFailures() {
            return !failure.isEmpty();
        }
    }
}
