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
package com.phonepe.columbus.elastic9.storage.handlers;

import com.phonepe.columbus.dao.index.Index;
import com.phonepe.columbus.dao.index.IndexProperties;
import com.phonepe.columbus.search.sort.FieldSort;
import com.phonepe.columbus.search.sort.SortOrder;
import com.phonepe.columbus.storage.StorageClient;
import com.phonepe.columbus.storage.commands.entity.bulk.BulkInsertCommand;
import com.phonepe.columbus.storage.commands.index.CreateIndexCommand;
import com.phonepe.columbus.storage.commands.index.DeleteIndexCommand;
import com.phonepe.columbus.storage.commands.search.ScanCommand;
import com.phonepe.columbus.storage.models.RequestExecutionPolicy;
import com.phonepe.columbus.storage.models.entity.Entity;
import com.phonepe.columbus.storage.models.result.StoredEntity;
import com.phonepe.columbus.type.EntityType;
import lombok.experimental.UtilityClass;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@UtilityClass
class IndexHelpers {

    static void createIndex(final StorageClient storage, final Index index, final Map<String, Object> schema) {
        CreateIndexCommand.builder()
                .index(index)
                .properties(IndexProperties.builder()
                        .numberOfShards(1)
                        .numberOfReplicas(0)
                        .schema(schema)
                        .build())
                .build()
                .execute(storage);
    }

    static void deleteIndex(final StorageClient storage, final Index index) {
        DeleteIndexCommand.builder()
                .index(index)
                .build()
                .execute(storage);
    }

    static void insertEntities(final StorageClient storage, final Index index, final ElasticEntity... entities) {
        insertEntities(storage, index, Arrays.asList(entities));
    }

    static void insertEntities(final StorageClient storage, final Index index, final List<ElasticEntity> entities) {
        insertAll(storage, index, entities.stream().map(EntityHelpers::entity).toList());
    }

    static void insertAll(final StorageClient storage, final Index index, final List<Entity<ElasticEntity>> entities) {
        BulkInsertCommand.<ElasticEntity>builder()
                .index(index)
                .entities(entities)
                .executionPolicy(RequestExecutionPolicy.FORCE_COMPLETION)
                .build()
                .execute(storage);
    }

    static Stream<ElasticEntity> readEntities(final StorageClient storage, final Index index) {
        return readAll(storage, index, EntityHelpers.ENTITY_TYPE).map(StoredEntity::getValue);
    }

    static <T> Stream<StoredEntity<T>> readAll(
            final StorageClient storage,
            final Index index,
            final EntityType<T> type
    ) {
        return ScanCommand.forType(type)
                .index(index)
                .sort(FieldSort.builder()
                        .order(SortOrder.ASC)
                        .field("id")
                        .build())
                .build()
                .execute(storage)
                .stream();
    }
}
