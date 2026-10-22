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

import co.elastic.clients.elasticsearch._types.ErrorCause;
import co.elastic.clients.elasticsearch.core.BulkRequest;
import co.elastic.clients.elasticsearch.core.BulkResponse;
import co.elastic.clients.elasticsearch.core.MgetRequest;
import co.elastic.clients.elasticsearch.core.MgetResponse;
import co.elastic.clients.elasticsearch.core.bulk.BulkResponseItem;
import co.elastic.clients.elasticsearch.core.bulk.OperationType;
import co.elastic.clients.elasticsearch.core.get.GetResult;
import co.elastic.clients.elasticsearch.core.mget.MultiGetError;
import co.elastic.clients.elasticsearch.core.mget.MultiGetResponseItem;
import com.phonepe.columbus.core.toolkit.Toolkit;
import com.phonepe.columbus.dao.id.Ids;
import com.phonepe.columbus.dao.id.ValueId;
import com.phonepe.columbus.dao.index.IndexName;
import com.phonepe.columbus.dao.index.IndexProperties;
import com.phonepe.columbus.dao.index.Indices;
import com.phonepe.columbus.dao.misc.OperationFailure;
import com.phonepe.columbus.dao.node.NodeIds;
import com.phonepe.columbus.elastic9.internal.storage.ColumbusElasticClient;
import com.phonepe.columbus.elastic9.internal.storage.handlers.entity.bulk.BulkDeleteHandler;
import com.phonepe.columbus.elastic9.internal.storage.handlers.entity.bulk.BulkGetHandler;
import com.phonepe.columbus.elastic9.internal.storage.handlers.entity.bulk.BulkInsertHandler;
import com.phonepe.columbus.elastic9.internal.storage.handlers.entity.bulk.BulkUpsertHandler;
import com.phonepe.columbus.elastic9.storage.toolkit.ElasticToolkit;
import com.phonepe.columbus.elastic9.test.annotations.WithElasticClient;
import com.phonepe.columbus.elastic9.test.annotations.WithElasticToolkit;
import com.phonepe.columbus.search.misc.query.Projection;
import com.phonepe.columbus.storage.StorageClient;
import com.phonepe.columbus.storage.commands.entity.bulk.BulkDeleteCommand;
import com.phonepe.columbus.storage.commands.entity.bulk.BulkGetCommand;
import com.phonepe.columbus.storage.commands.entity.bulk.BulkInsertCommand;
import com.phonepe.columbus.storage.commands.entity.bulk.BulkUpsertCommand;
import com.phonepe.columbus.storage.commands.index.CreateIndexCommand;
import com.phonepe.columbus.storage.commands.index.DeleteIndexCommand;
import com.phonepe.columbus.storage.models.RequestExecutionPolicy;
import com.phonepe.columbus.storage.models.entity.Entity;
import com.phonepe.columbus.storage.models.entity.EntityId;
import com.phonepe.columbus.storage.models.result.UpsertResult;
import com.phonepe.columbus.test.annotations.WithMockito;
import com.phonepe.columbus.test.annotations.WithUniqueString;
import com.phonepe.columbus.test.annotations.WithUniqueString.Inject;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Answers;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WithMockito
@WithElasticClient.All
@WithElasticToolkit.All
class BulkEntityCommandsTest {

    private static final ValueId ID_1 = Ids.value("columbus-mock-entity-id-1");
    private static final ValueId ID_2 = Ids.value("columbus-mock-entity-id-2");
    private static final ElasticEntity ENTITY_1 = ElasticEntity.builder()
            .id(ID_1.getValue())
            .name("mock-entity")
            .count(-1)
            .build();
    private static final ElasticEntity ENTITY_2 = ElasticEntity.builder()
            .id(ID_2.getValue())
            .name("mock-entity")
            .count(-1)
            .build();

    private static void verifyElasticSearchFailure(final OperationFailure failure, final String message) {
        assertAll(
                "failure",
                () -> assertNotNull(failure, message + " must fail"),
                () -> assertFalse(failure.getMessage().isEmpty(), "must have a message"));
    }

    @Nested
    @WithUniqueString(format = "columbus-group-%d-id-")
    class SuccessTest {

        private static final IndexName INDEX = Indices.name("columbus.bulk.entity.commands.test.data.index");

        @Test
        @DisplayName("insert and get entities")
        void testInsertAndGet(final StorageClient storage, @Inject final String groupId) {
            final var id1 = Ids.value(groupId + 1);
            final var id2 = Ids.value(groupId + 2);
            final var entity1 = ElasticEntity.builder()
                    .id(id1.getValue())
                    .name("bulk-insert-get-1")
                    .count(1)
                    .build();
            final var entity2 = ElasticEntity.builder()
                    .id(id2.getValue())
                    .name("bulk-insert-get-2")
                    .count(2)
                    .build();
            final var bulkInsert = BulkInsertCommand.<ElasticEntity>builder()
                    .index(INDEX)
                    .entity(Entity.<ElasticEntity>builder()
                            .id(id1)
                            .nodeId(NodeIds.shard(id1.getValue(), id1.getValue()))
                            .value(entity1)
                            .build())
                    .entity(Entity.<ElasticEntity>builder()
                            .id(id2)
                            .nodeId(NodeIds.shard(id2.getValue()))
                            .value(entity2)
                            .build())
                    .executionPolicy(RequestExecutionPolicy.WAIT_FOR_COMPLETION)
                    .build();

            final var bulkGet = BulkGetCommand.forType(EntityHelpers.ENTITY_TYPE)
                    .index(INDEX)
                    .id(EntityId.builder()
                            .id(id1)
                            .nodeId(NodeIds.shard(id1.getValue(), id1.getValue()))
                            .build())
                    .id(EntityId.builder()
                            .id(id2)
                            .nodeId(NodeIds.routingKeys(List.of(id2.getValue(), id1.getValue()), id2.getValue()))
                            .build())
                    .build();

            final var insertResponse = bulkInsert.execute(storage);
            final var getResponse = bulkGet.execute(storage);

            assertFalse(insertResponse.hasFailures(), "bulk insert must succeed without failures");
            assertAll(
                    "bulk get",
                    () -> assertFalse(getResponse.hasFailures(), "bulk get must succeed without failures"),
                    () -> assertEquals(entity1, getResponse.getEntities().get(id1), "entity 1 must match inserted"),
                    () -> assertEquals(entity2, getResponse.getEntities().get(id2), "entity 2 must match inserted"));
        }

        @Test
        @DisplayName("upsert and get entities")
        void testUpsertAndGet(final StorageClient storage, @Inject final String groupId) {
            final var id1 = Ids.value(groupId + 1);
            final var id2 = Ids.value(groupId + 2);
            final var oldEntity1 = ElasticEntity.builder()
                    .id(id1.getValue())
                    .name("bulk-upsert-get-1")
                    .count(1)
                    .build();
            final var newEntity1 = ElasticEntity.builder()
                    .id(id1.getValue())
                    .name("bulk-upsert-get-1")
                    .count(10)
                    .build();
            final var newEntity2 = ElasticEntity.builder()
                    .id(id2.getValue())
                    .name("bulk-upsert-get-2-update")
                    .count(2)
                    .build();
            final var bulkInsert = BulkInsertCommand.<ElasticEntity>builder()
                    .index(INDEX)
                    .entity(Entity.<ElasticEntity>builder()
                            .id(id1)
                            .nodeId(NodeIds.shard(id1.getValue(), id1.getValue()))
                            .value(oldEntity1)
                            .build())
                    .executionPolicy(RequestExecutionPolicy.FORCE_COMPLETION)
                    .build();
            final var bulkUpsert = BulkUpsertCommand.<ElasticEntity>builder()
                    .index(INDEX)
                    .entity(Entity.<ElasticEntity>builder()
                            .id(id1)
                            .nodeId(NodeIds.shard(id1.getValue(), id1.getValue()))
                            .value(newEntity1)
                            .build())
                    .entity(Entity.<ElasticEntity>builder()
                            .id(id2)
                            .nodeId(NodeIds.shard(id2.getValue()))
                            .value(newEntity2)
                            .build())
                    .executionPolicy(RequestExecutionPolicy.SUBMIT)
                    .build();

            final var bulkGet = BulkGetCommand.forType(EntityHelpers.ENTITY_TYPE)
                    .index(INDEX)
                    .id(EntityId.builder().id(id1).build())
                    .id(EntityId.builder().id(id2).build())
                    .build();

            final var insertResponse = bulkInsert.execute(storage);
            final var getBefore = bulkGet.execute(storage);
            final var upsertResponse = bulkUpsert.execute(storage);
            final var getAfter = bulkGet.execute(storage);

            assertFalse(insertResponse.hasFailures(), "bulk insert must succeed without failures");
            assertAll(
                    "bulk get before",
                    () -> assertTrue(getBefore.hasFailures(), "bulk get must have an entity missing"),
                    () -> assertEquals(oldEntity1, getBefore.getEntities().get(id1), "entity 1 must match inserted"),
                    () -> assertTrue(getBefore.getNotFound().contains(id2), "entity 2 must not exist"));
            assertAll(
                    "bulk upsert",
                    () -> assertFalse(upsertResponse.hasFailures(), "bulk upsert must succeed without failures"),
                    () -> assertEquals(
                            UpsertResult.UPDATED, upsertResponse.getSuccess().get(id1),
                            "entity 1 must be updated"),
                    () -> assertEquals(
                            UpsertResult.CREATED, upsertResponse.getSuccess().get(id2),
                            "entity 2 must be created"));
            assertAll(
                    "bulk get after",
                    () -> assertFalse(getAfter.hasFailures(), "bulk get must succeed without failures"),
                    () -> assertEquals(newEntity1, getAfter.getEntities().get(id1), "entity 1 must be updated"),
                    () -> assertEquals(newEntity2, getAfter.getEntities().get(id2), "entity 2 must be inserted"));
        }

        @Test
        @DisplayName("insert and delete entities")
        void testInsertAndDelete(final StorageClient storage, @Inject final String groupId) {
            final var id1 = Ids.value(groupId + 1);
            final var id2 = Ids.value(groupId + 2);
            final var entity1 = ElasticEntity.builder()
                    .id(id1.getValue())
                    .name("bulk-insert-delete-1")
                    .count(1)
                    .build();
            final var entity2 = ElasticEntity.builder()
                    .id(id2.getValue())
                    .name("bulk-insert-delete-2")
                    .count(2)
                    .build();
            final var bulkInsert = BulkInsertCommand.<ElasticEntity>builder()
                    .index(INDEX)
                    .entity(Entity.<ElasticEntity>builder()
                            .id(id1)
                            .nodeId(NodeIds.shard(id1.getValue(), id1.getValue()))
                            .value(entity1)
                            .build())
                    .entity(Entity.<ElasticEntity>builder()
                            .id(id2)
                            .nodeId(NodeIds.shard(id2.getValue()))
                            .value(entity2)
                            .build())
                    .build();
            final var bulkDelete = BulkDeleteCommand.builder()
                    .index(INDEX)
                    .id(EntityId.builder().id(id1).build())
                    .id(EntityId.builder().id(id2).build())
                    .executionPolicy(RequestExecutionPolicy.SUBMIT)
                    .build();

            final var bulkGet = BulkGetCommand.forType(EntityHelpers.ENTITY_TYPE)
                    .index(INDEX)
                    .id(EntityId.builder().id(id1).build())
                    .id(EntityId.builder().id(id2).build())
                    .build();

            final var insertResponse = bulkInsert.execute(storage);
            final var getBefore = bulkGet.execute(storage);
            final var deleteResponse = bulkDelete.execute(storage);
            final var getAfter = bulkGet.execute(storage);

            assertFalse(insertResponse.hasFailures(), "bulk insert must succeed without failures");
            assertAll(
                    "bulk get before",
                    () -> assertFalse(getBefore.hasFailures(), "bulk get must succeed without failures"),
                    () -> assertEquals(entity1, getBefore.getEntities().get(id1), "entity 1 must match inserted"),
                    () -> assertEquals(entity2, getBefore.getEntities().get(id2), "entity 2 must match inserted"));
            assertFalse(deleteResponse.hasFailures(), "bulk upsert must succeed without failures");
            assertAll(
                    "bulk get after",
                    () -> assertTrue(getAfter.hasFailures(), "bulk get must fail for missing ids"),
                    () -> assertTrue(getAfter.getNotFound().contains(id1), "entity 1 must not exist"),
                    () -> assertTrue(getAfter.getNotFound().contains(id2), "entity 2 must not exist"));
        }

        @Test
        @DisplayName("delete non-existent entities")
        void testDeleteMissing(final StorageClient storage, @Inject final String groupId) {
            final var id1 = Ids.value(groupId + 1);
            final var id2 = Ids.value(groupId + 2);
            final var bulkDelete = BulkDeleteCommand.builder()
                    .index(INDEX)
                    .id(EntityId.builder().id(id1).build())
                    .id(EntityId.builder().id(id2).build())
                    .build();

            final var deleteResponse = bulkDelete.execute(storage);

            assertTrue(deleteResponse.hasFailures(), "bulk delete must have failures for missing entities");
            assertAll(
                    "bulk delete",
                    () -> assertTrue(deleteResponse.hasFailures(), "bulk delete must fail for missing entities"),
                    () -> assertTrue(deleteResponse.getNotFound().contains(id1), "entity 1 must not be found"),
                    () -> assertTrue(deleteResponse.getNotFound().contains(id2), "entity 2 must not be found"));
        }

        @Test
        @DisplayName("insert and get projection")
        void testInsertAndGetProjection(final StorageClient storage, @Inject final String groupId) {
            final var id1 = Ids.value(groupId + 1);
            final var id2 = Ids.value(groupId + 2);
            final var entity1 = ElasticEntity.builder()
                    .id(id1.getValue())
                    .name("bulk-insert-get-projection-1")
                    .count(1)
                    .build();
            final var entity2 = ElasticEntity.builder()
                    .id(id2.getValue())
                    .name("bulk-insert-get-projection-2")
                    .count(2)
                    .build();
            final var projectedEntity1 = ElasticEntity.builder()
                    .id(id1.getValue())
                    .count(1)
                    .build();
            final var projectedEntity2 = ElasticEntity.builder()
                    .id(id2.getValue())
                    .count(2)
                    .build();
            final var bulkInsert = BulkInsertCommand.<ElasticEntity>builder()
                    .index(INDEX)
                    .entity(Entity.<ElasticEntity>builder()
                            .id(id1)
                            .value(entity1)
                            .build())
                    .entity(Entity.<ElasticEntity>builder()
                            .id(id2)
                            .value(entity2)
                            .build())
                    .build();

            final var bulkGet = BulkGetCommand.forType(EntityHelpers.ENTITY_TYPE)
                    .index(INDEX)
                    .id(EntityId.builder().id(id1).build())
                    .id(EntityId.builder().id(id2).build())
                    .projection(Projection.builder()
                            .excludeField("name")
                            .build())
                    .build();

            final var insertResponse = bulkInsert.execute(storage);
            final var getResponse = bulkGet.execute(storage);

            assertFalse(insertResponse.hasFailures(), "bulk insert must succeed without failures");
            assertAll(
                    "bulk get",
                    () -> assertFalse(getResponse.hasFailures(), "bulk get must succeed without failures"),
                    () -> assertEquals(
                            projectedEntity1, getResponse.getEntities().get(id1),
                            "entity 1 must match inserted"),
                    () -> assertEquals(
                            projectedEntity2, getResponse.getEntities().get(id2),
                            "entity 2 must match inserted"));
        }

        @Test
        @DisplayName("insert data with invalid type")
        void testInsertInvalidType(final StorageClient storage, @Inject final String groupId) {
            final var id1 = Ids.value(groupId + 1);
            final var id2 = Ids.value(groupId + 2);
            final var entity1 = ElasticEntity.builder()
                    .id(id1.getValue())
                    .name("bulk-insert-invalid-1")
                    .count(1)
                    .attribute("dummy")
                    .build();
            final var entity2 = ElasticEntity.builder()
                    .id(id2.getValue())
                    .name("bulk-insert-invalid-2")
                    .count(2)
                    .attribute(10)
                    .build();
            final var bulkInsert = BulkInsertCommand.<ElasticEntity>builder()
                    .index(INDEX)
                    .entity(Entity.<ElasticEntity>builder()
                            .id(id1)
                            .value(entity1)
                            .build())
                    .entity(Entity.<ElasticEntity>builder()
                            .id(id2)
                            .value(entity2)
                            .build())
                    .build();

            final var bulkGet = BulkGetCommand.forType(EntityHelpers.ENTITY_TYPE)
                    .index(INDEX)
                    .id(EntityId.builder().id(id1).build())
                    .id(EntityId.builder().id(id2).build())
                    .build();

            final var insertResponse = bulkInsert.execute(storage);
            final var getResponse = bulkGet.execute(storage);

            assertAll(
                    "bulk insert",
                    () -> assertTrue(
                            insertResponse.hasFailures(),
                            "bulk insert must have failures for invalid entities"),
                    () -> verifyElasticSearchFailure(insertResponse.getFailure().get(id1), "insertion of entity 1"));
            assertAll(
                    "bulk get",
                    () -> assertTrue(getResponse.hasFailures(), "bulk get must fail for missing entities"),
                    () -> assertTrue(getResponse.getNotFound().contains(id1), "entity 1 must not be found"),
                    () -> assertEquals(entity2, getResponse.getEntities().get(id2), "entity 2 must match inserted"));
        }

        @Test
        @DisplayName("upsert data with invalid type")
        void testUpsertInvalidType(final StorageClient storage, @Inject final String groupId) {
            final var id1 = Ids.value(groupId + 1);
            final var id2 = Ids.value(groupId + 2);
            final var entity1 = ElasticEntity.builder()
                    .id(id1.getValue())
                    .name("bulk-upsert-invalid-1")
                    .count(1)
                    .attribute("dummy")
                    .build();
            final var entity2 = ElasticEntity.builder()
                    .id(id2.getValue())
                    .name("bulk-upsert-invalid-2")
                    .count(2)
                    .attribute(10)
                    .build();
            final var bulkUpsert = BulkUpsertCommand.<ElasticEntity>builder()
                    .index(INDEX)
                    .entity(Entity.<ElasticEntity>builder()
                            .id(id1)
                            .value(entity1)
                            .build())
                    .entity(Entity.<ElasticEntity>builder()
                            .id(id2)
                            .value(entity2)
                            .build())
                    .build();

            final var bulkGet = BulkGetCommand.forType(EntityHelpers.ENTITY_TYPE)
                    .index(INDEX)
                    .id(EntityId.builder().id(id1).build())
                    .id(EntityId.builder().id(id2).build())
                    .build();

            final var upsertResponse = bulkUpsert.execute(storage);
            final var getResponse = bulkGet.execute(storage);

            assertAll(
                    "bulk upsert",
                    () -> assertTrue(
                            upsertResponse.hasFailures(),
                            "bulk upsert must have failures for invalid entities"),
                    () -> verifyElasticSearchFailure(upsertResponse.getFailure().get(id1), "upsert of entity 1"));
            assertAll(
                    "bulk get",
                    () -> assertTrue(getResponse.hasFailures(), "bulk get must fail for missing entities"),
                    () -> assertTrue(getResponse.getNotFound().contains(id1), "entity 1 must not be found"),
                    () -> assertEquals(entity2, getResponse.getEntities().get(id2), "entity 2 must match inserted"));
        }

        @BeforeAll
        static void beforeAll(final StorageClient storage) {
            CreateIndexCommand.builder()
                    .index(INDEX)
                    .properties(IndexProperties.builder()
                            .numberOfReplicas(0)
                            .schema(EntityHelpers.schemaWithAttributeType("integer"))
                            .build())
                    .build()
                    .execute(storage);
        }

        @AfterAll
        static void afterAll(final StorageClient storage) {
            DeleteIndexCommand.builder()
                    .index(INDEX)
                    .build()
                    .execute(storage);
        }
    }

    @Nested
    class FailureTest {

        private static final IndexName INDEX = Indices.name("columbus.bulk.entity.commands.test.mock.index");

        @Mock(answer = Answers.RETURNS_DEEP_STUBS)
        private ColumbusElasticClient storage;

        @BeforeEach
        void setUp(final Toolkit toolkit) {
            when(storage.toolkit().serDe()).thenReturn(toolkit.serDe());
            when(storage.toolkit().indexResolver().resolve(INDEX).getName()).thenReturn(INDEX.getName());
            when(storage.toolkit().idResolver().resolve(ID_1).getId()).thenReturn(ID_1.getValue());
            when(storage.toolkit().idResolver().resolve(ID_2).getId()).thenReturn(ID_2.getValue());
        }

        @Test
        @DisplayName("Insert")
        void testInsertCommand() throws IOException {
            final var command = BulkInsertCommand.<ElasticEntity>builder()
                    .index(INDEX)
                    .entity(Entity.<ElasticEntity>builder()
                            .id(ID_1)
                            .value(ENTITY_1)
                            .build())
                    .entity(Entity.<ElasticEntity>builder()
                            .id(ID_2)
                            .value(ENTITY_2)
                            .build())
                    .build();

            when(storage.client().bulk(any(BulkRequest.class))).thenThrow(IOException.class);

            final var handler = new BulkInsertHandler<ElasticEntity, ColumbusElasticClient>();

            assertThrows(IOException.class, () -> handler.handle(command, storage), "must throw on client failure");
        }

        @Test
        @DisplayName("Get")
        void testGetCommand() throws IOException {
            final var command = BulkGetCommand.forType(EntityHelpers.ENTITY_TYPE)
                    .index(INDEX)
                    .id(EntityId.builder().id(ID_1).build())
                    .id(EntityId.builder().id(ID_2).build())
                    .build();

            when(storage.client().mget(any(MgetRequest.class), ArgumentMatchers.<Type>any()))
                    .thenThrow(IOException.class);

            final var handler = new BulkGetHandler<ElasticEntity, ColumbusElasticClient>();

            assertThrows(IOException.class, () -> handler.handle(command, storage), "must throw on client failure");
        }

        @Test
        @DisplayName("Upsert")
        void testUpsertCommand() throws IOException {
            final var command = BulkUpsertCommand.<ElasticEntity>builder()
                    .index(INDEX)
                    .entity(Entity.<ElasticEntity>builder()
                            .id(ID_1)
                            .value(ENTITY_1)
                            .build())
                    .entity(Entity.<ElasticEntity>builder()
                            .id(ID_2)
                            .value(ENTITY_2)
                            .build())
                    .build();

            when(storage.client().bulk(any(BulkRequest.class))).thenThrow(IOException.class);

            final var handler = new BulkUpsertHandler<ElasticEntity, ColumbusElasticClient>();

            assertThrows(IOException.class, () -> handler.handle(command, storage), "must throw on client failure");
        }

        @Test
        @DisplayName("Delete")
        void testDeleteCommand() throws IOException {
            final var command = BulkDeleteCommand.builder()
                    .index(INDEX)
                    .id(EntityId.builder().id(ID_1).build())
                    .id(EntityId.builder().id(ID_2).build())
                    .build();

            when(storage.client().bulk(any(BulkRequest.class))).thenThrow(IOException.class);

            final var handler = new BulkDeleteHandler<ColumbusElasticClient>();

            assertThrows(IOException.class, () -> handler.handle(command, storage), "must throw on client failure");
        }
    }

    @Nested
    class SimulatedTest {

        private static final IndexName INDEX = Indices.name("columbus.bulk.entity.commands.test.simulation.index");

        @Mock(answer = Answers.RETURNS_DEEP_STUBS)
        private ColumbusElasticClient storage;

        @BeforeEach
        void setUp(final ElasticToolkit toolkit) {
            when(storage.toolkit()).thenReturn(toolkit);
        }

        @Test
        @DisplayName("Get entity failures")
        void testGetCommandFailures() throws IOException {
            final var command = BulkGetCommand.forType(EntityHelpers.ENTITY_TYPE)
                    .index(INDEX)
                    .id(EntityId.builder().id(ID_1).build())
                    .id(EntityId.builder().id(ID_2).build())
                    .build();

            final var response = new MgetResponse.Builder<ElasticEntity>().docs(List.of(
                            new MultiGetResponseItem.Builder<ElasticEntity>()
                                    .result(new GetResult.Builder<ElasticEntity>()
                                            .id(ID_1.getValue())
                                            .index(INDEX.getName())
                                            .source(ENTITY_1)
                                            .found(true)
                                            .build())
                                    .build(),
                            new MultiGetResponseItem.Builder<ElasticEntity>()
                                    .failure(new MultiGetError.Builder()
                                            .id(ID_2.getValue())
                                            .index(INDEX.getName())
                                            .error(new ErrorCause.Builder()
                                                    .reason("ElasticsearchException")
                                                    .type("ElasticsearchException")
                                                    .build())
                                            .build())
                                    .build()))
                    .build();

            when(storage.client().<ElasticEntity>mget(any(MgetRequest.class), ArgumentMatchers.<Type>any()))
                    .thenReturn(response);

            final var handler = new BulkGetHandler<ElasticEntity, ColumbusElasticClient>();
            final var get = handler.handle(command, storage);

            assertAll(
                    "bulk get",
                    () -> assertTrue(get.hasFailures(), "response failures must be propagated"),
                    () -> assertEquals(ENTITY_1, get.getEntities().get(ID_1), "entity 1 must match"),
                    () -> assertTrue(get.getNotFound().isEmpty(), "entities must be found"),
                    () -> assertEquals(1, get.getFailure().size(), "must have exactly one failure"),
                    () -> verifyElasticSearchFailure(get.getFailure().get(ID_2), "get of entity 2"));
        }

        @Test
        @DisplayName("Delete entity failures")
        void testDeleteCommandFailures() throws IOException {
            final var command = BulkDeleteCommand.builder()
                    .index(INDEX)
                    .id(EntityId.builder().id(ID_1).build())
                    .id(EntityId.builder().id(ID_2).build())
                    .build();

            final var response = new BulkResponse.Builder()
                    .errors(true)
                    .took(1L)
                    .items(new BulkResponseItem.Builder()
                            .index(INDEX.getName())
                            .id(ID_1.getValue())
                            .operationType(OperationType.Delete)
                            .status(200)
                            .result("deleted")
                            .build())
                    .items(new BulkResponseItem.Builder()
                            .index(INDEX.getName())
                            .id(ID_2.getValue())
                            .operationType(OperationType.Delete)
                            .status(400)
                            .error(new ErrorCause.Builder()
                                    .reason("ElasticsearchException")
                                    .type("ElasticsearchException")
                                    .build())
                            .build())
                    .build();

            when(storage.client().bulk(any(BulkRequest.class))).thenReturn(response);

            final var handler = new BulkDeleteHandler<ColumbusElasticClient>();
            final var delete = handler.handle(command, storage);

            assertAll(
                    "bulk delete",
                    () -> assertTrue(delete.hasFailures(), "response failures must be propagated"),
                    () -> assertTrue(delete.getNotFound().isEmpty(), "entities must be found"),
                    () -> assertEquals(1, delete.getFailure().size(), "must have exactly one failure"),
                    () -> verifyElasticSearchFailure(delete.getFailure().get(ID_2), "deletion of entity 2"));
        }
    }
}
