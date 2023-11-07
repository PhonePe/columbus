package com.phonepe.platform.columbus.storage.commands.entity.bulk;

import com.phonepe.platform.columbus.command.Command;
import com.phonepe.platform.columbus.dao.id.Id;
import com.phonepe.platform.columbus.dao.index.Index;
import com.phonepe.platform.columbus.dao.misc.OperationFailure;
import com.phonepe.platform.columbus.search.misc.query.Projection;
import com.phonepe.platform.columbus.storage.models.entity.EntityId;
import com.phonepe.platform.columbus.type.EntityType;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.Singular;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * A command to get a batch of entities from an index.
 */
@Data
@Builder
public class BulkGetCommand<T> implements Command<BulkGetCommand.Result<T>> {

    public static final String COMMAND_NAME = "BULK_GET";

    @NonNull
    private final Index index;

    @Singular(ignoreNullCollections = true)
    private final List<@NonNull EntityId> ids;

    private final Projection projection;

    @NonNull
    private final EntityType<T> entityType;

    public static <T> BulkGetCommandBuilder<T> forType(final EntityType<T> type) {
        return BulkGetCommand.<T>builder()
                .entityType(type);
    }

    @Override
    public String name() {
        return COMMAND_NAME;
    }

    @Data
    @Builder
    public static class Result<T> {

        @Singular(ignoreNullCollections = true)
        private final Map<Id, T> entities;

        @Singular(value = "notFound", ignoreNullCollections = true)
        private final Set<Id> notFound;

        @Singular(value = "failure", ignoreNullCollections = true)
        private final Map<Id, OperationFailure> failure;

        public boolean hasFailures() {
            return !failure.isEmpty() || !notFound.isEmpty();
        }
    }
}
