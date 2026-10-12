package com.phonepe.columbus.storage.commands.entity.bulk;

import com.phonepe.columbus.command.Command;
import com.phonepe.columbus.dao.index.Index;
import com.phonepe.columbus.dao.misc.EntityOperationFailure;
import com.phonepe.columbus.dao.misc.IndexOperationFailure;
import com.phonepe.columbus.search.misc.query.Projection;
import com.phonepe.columbus.search.query.Query;
import com.phonepe.columbus.storage.models.RequestExecutionPolicy;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.Singular;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * A command to copy from one or more source indices to another.
 * <p>
 * Copy only a subset of data by providing {@link #query} or a subset of fields by providing {@link #projection}.
 */
@Data
@Builder
@SuppressWarnings("java:S1170") // Sonar does not identify Lombok's builder default
public class CopyCommand implements Command<CopyCommand.Result> {

    public static final String COMMAND_NAME = "COPY";

    @Singular(ignoreNullCollections = true)
    private final List<@NonNull Index> sourceIndices;

    @NonNull
    private final Index destinationIndex;

    /**
     * Filter the documents to be copied, copies all the documents by default.
     */
    private final Query query;

    /**
     * Select the fields of the documents to be copied, copies all the fields by default.
     */
    private final Projection projection;

    /**
     * If set to false, any failure will stop the execution.
     */
    @Builder.Default
    @Accessors(fluent = true)
    private final boolean failOnError = true;

    /**
     * If set to true, documents are only copied if they don't exist in the destination.
     */
    @Builder.Default
    @Accessors(fluent = true)
    private final boolean skipExisting = false;

    /**
     * Maximum number of documents to copy. This limits the number of successful writes and not the number of reads.
     * Any failed documents are not counted.
     */
    private final Integer maximumCount;

    private final Integer batchSize;

    private final RequestExecutionPolicy executionPolicy;

    @Override
    public String name() {
        return COMMAND_NAME;
    }

    @Data
    @Builder
    public static class Result {

        private final String taskId;

        private final long total;

        private final long created;

        private final long updated;

        @Singular(ignoreNullCollections = true)
        private final List<EntityOperationFailure> writeFailures;

        @Singular(ignoreNullCollections = true)
        private final List<IndexOperationFailure> searchFailures;

        public boolean hasFailures() {
            return !writeFailures.isEmpty() || !searchFailures.isEmpty();
        }
    }
}
