package com.phonepe.commons.columbus.storage.commands.search;

import com.phonepe.commons.columbus.command.Command;
import com.phonepe.commons.columbus.dao.index.Index;
import com.phonepe.commons.columbus.dao.node.NodeId;
import com.phonepe.commons.columbus.search.misc.query.Projection;
import com.phonepe.commons.columbus.search.query.Query;
import com.phonepe.commons.columbus.search.sort.Sort;
import com.phonepe.commons.columbus.storage.commands.search.ScanCommand.Scanner;
import com.phonepe.commons.columbus.storage.models.result.StoredEntity;
import com.phonepe.commons.columbus.type.EntityType;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.Singular;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

/**
 * A command to scan entities in an index.
 */
@Data
@Builder
public class ScanCommand<T> implements Command<Scanner<T>> {

    public static final String COMMAND_NAME = "SCAN";

    @Singular(ignoreNullCollections = true)
    private final List<@NonNull Index> indices;

    private final NodeId nodeId;

    private final Query query;

    @Singular(ignoreNullCollections = true)
    private final List<@NonNull Sort> sorts;

    private final Projection projection;

    @Builder.Default
    private final int batchSize = 1000;

    @Builder.Default
    private final long durationInSeconds = 60;

    @NonNull
    private final EntityType<T> entityType;

    public static <T> ScanCommandBuilder<T> forType(final EntityType<T> type) {
        return ScanCommand.<T>builder()
                .entityType(type);
    }

    @Override
    public String name() {
        return COMMAND_NAME;
    }

    public interface Scanner<T> {

        Iterator<StoredEntity<T>> iterate();

        Iterator<List<StoredEntity<T>>> iterateBatch(int batchSize);

        Stream<StoredEntity<T>> stream();

        Stream<List<StoredEntity<T>>> streamBatch(int batchSize);
    }
}
