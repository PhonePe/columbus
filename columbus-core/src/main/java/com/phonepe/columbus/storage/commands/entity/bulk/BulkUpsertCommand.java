package com.phonepe.columbus.storage.commands.entity.bulk;

import com.phonepe.columbus.command.Command;
import com.phonepe.columbus.dao.id.Id;
import com.phonepe.columbus.dao.index.Index;
import com.phonepe.columbus.dao.misc.OperationFailure;
import com.phonepe.columbus.storage.models.RequestExecutionPolicy;
import com.phonepe.columbus.storage.models.entity.Entity;
import com.phonepe.columbus.storage.models.result.UpsertResult;
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
