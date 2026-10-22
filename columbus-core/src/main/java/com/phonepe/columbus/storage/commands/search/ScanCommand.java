/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.phonepe.columbus.storage.commands.search;

import com.phonepe.columbus.command.Command;
import com.phonepe.columbus.dao.index.Index;
import com.phonepe.columbus.dao.node.NodeId;
import com.phonepe.columbus.search.misc.query.Projection;
import com.phonepe.columbus.search.query.Query;
import com.phonepe.columbus.search.sort.Sort;
import com.phonepe.columbus.storage.commands.search.ScanCommand.Scanner;
import com.phonepe.columbus.storage.models.result.StoredEntity;
import com.phonepe.columbus.type.EntityType;
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
@SuppressWarnings("java:S1170") // Sonar does not identify Lombok's builder default
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
