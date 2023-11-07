package com.phonepe.platform.columbus.elastic.storage.handlers;

import com.phonepe.platform.columbus.core.toolkit.Toolkit;
import com.phonepe.platform.columbus.dao.id.Ids;
import com.phonepe.platform.columbus.dao.id.ValueId;
import com.phonepe.platform.columbus.dao.index.IndexName;
import com.phonepe.platform.columbus.dao.index.IndexProperties;
import com.phonepe.platform.columbus.dao.index.Indices;
import com.phonepe.platform.columbus.dao.node.NodeIds;
import com.phonepe.platform.columbus.elastic.internal.storage.ColumbusElasticClient;
import com.phonepe.platform.columbus.elastic.internal.storage.handlers.entity.DeleteHandler;
import com.phonepe.platform.columbus.elastic.internal.storage.handlers.entity.GetHandler;
import com.phonepe.platform.columbus.elastic.internal.storage.handlers.entity.InsertHandler;
import com.phonepe.platform.columbus.elastic.internal.storage.handlers.entity.UpsertHandler;
import com.phonepe.platform.columbus.search.misc.query.Projection;
import com.phonepe.platform.columbus.storage.StorageClient;
import com.phonepe.platform.columbus.storage.commands.entity.DeleteCommand;
import com.phonepe.platform.columbus.storage.commands.entity.GetCommand;
import com.phonepe.platform.columbus.storage.commands.entity.InsertCommand;
import com.phonepe.platform.columbus.storage.commands.entity.UpsertCommand;
import com.phonepe.platform.columbus.storage.commands.index.CreateIndexCommand;
import com.phonepe.platform.columbus.storage.commands.index.DeleteIndexCommand;
import com.phonepe.platform.columbus.storage.models.result.UpsertResult;
import com.phonepe.platform.columbus.test.annotations.WithElasticClient;
import com.phonepe.platform.columbus.test.annotations.WithMockito;
import com.phonepe.platform.columbus.test.annotations.WithUniqueTestId;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Answers;
import org.mockito.Mock;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WithMockito
@WithElasticClient.All
class EntityCommandsTest {

    @Nested
    @WithUniqueTestId(format = "columbus-entity-%02d")
    class SuccessTest {

        private static final IndexName INDEX = Indices.name("columbus.entity.commands.test.data.index");

        @Test
        @DisplayName("get non-existent entity")
        void testGetMissing(final StorageClient storage, final ValueId id) {
            final var entity = GetCommand.forType(EntityHelpers.ENTITY_TYPE)
                    .index(INDEX)
                    .id(id)
                    .nodeId(NodeIds.shard(id.getValue(), id.getValue()))
                    .build()
                    .execute(storage);

            assertTrue(entity.isEmpty(), "entity must not exist");
        }

        @Test
        @DisplayName("insert and get entity")
        void testInsertAndGet(final StorageClient storage, final ValueId id) {
            final var getCommand = GetCommand.forType(EntityHelpers.ENTITY_TYPE)
                    .index(INDEX)
                    .id(id)
                    .build();
            final var entity = ElasticEntity.builder()
                    .id(id.getValue())
                    .name("insert-get")
                    .count(1)
                    .build();
            final var insertCommand = InsertCommand.withValue(entity)
                    .index(INDEX)
                    .id(id)
                    .nodeId(NodeIds.shard(id.getValue(), id.getValue()))
                    .build();

            final var entityBefore = getCommand.execute(storage);
            insertCommand.execute(storage);
            final var entityAfter = getCommand.execute(storage);

            assertTrue(entityBefore.isEmpty(), "entity must not exist before insertion");
            assertAll(
                    "inserted entity",
                    () -> assertTrue(entityAfter.isPresent(), "entity must exist after insertion"),
                    () -> assertEquals(entity, entityAfter.orElse(null), "entity must match inserted value"));
        }

        @Test
        @DisplayName("upsert non-existent entity")
        void testUpsertMissing(final StorageClient storage, final ValueId id) {
            final var getCommand = GetCommand.forType(EntityHelpers.ENTITY_TYPE)
                    .index(INDEX)
                    .id(id)
                    .build();
            final var entity = ElasticEntity.builder()
                    .id(id.getValue())
                    .name("upsert-non-existent")
                    .count(2)
                    .build();
            final var upsertCommand = UpsertCommand.withValue(entity)
                    .index(INDEX)
                    .id(id)
                    .nodeId(NodeIds.shard(id.getValue(), id.getValue()))
                    .build();

            final var entityBefore = getCommand.execute(storage);
            final var upsertResult = upsertCommand.execute(storage);
            final var entityAfter = getCommand.execute(storage);

            assertTrue(entityBefore.isEmpty(), "entity must not exist before upsert");
            assertEquals(UpsertResult.CREATED, upsertResult, "entity must be created");
            assertAll(
                    "created entity",
                    () -> assertTrue(entityAfter.isPresent(), "entity must exist after upsert"),
                    () -> assertEquals(entity, entityAfter.orElse(null), "entity must match inserted value"));
        }

        @Test
        @DisplayName("upsert existing entity")
        void testUpsertExisting(final StorageClient storage, final ValueId id) {
            final var getCommand = GetCommand.forType(EntityHelpers.ENTITY_TYPE)
                    .index(INDEX)
                    .id(id)
                    .build();
            final var oldEntity = ElasticEntity.builder()
                    .id(id.getValue())
                    .name("inserted")
                    .count(3)
                    .build();
            final var insertCommand = InsertCommand.withValue(oldEntity)
                    .index(INDEX)
                    .id(id)
                    .build();
            final var newEntity = ElasticEntity.builder()
                    .id(id.getValue())
                    .name("upsert-existing")
                    .count(4)
                    .build();
            final var upsertCommand = UpsertCommand.withValue(newEntity)
                    .index(INDEX)
                    .id(id)
                    .build();

            insertCommand.execute(storage);
            final var entityBefore = getCommand.execute(storage);
            final var upsertResult = upsertCommand.execute(storage);
            final var entityAfter = getCommand.execute(storage);

            assertAll(
                    "existing entity",
                    () -> assertTrue(entityBefore.isPresent(), "entity must exist before upsert"),
                    () -> assertEquals(oldEntity, entityBefore.orElse(null), "entity must match inserted value"));
            assertEquals(UpsertResult.UPDATED, upsertResult, "entity must be updated");
            assertAll(
                    "updated entity",
                    () -> assertTrue(entityAfter.isPresent(), "entity must exist after upsert"),
                    () -> assertEquals(newEntity, entityAfter.orElse(null), "entity must match updated value"));
        }

        @Test
        @DisplayName("delete non-existent entity")
        void testDeleteMissing(final StorageClient storage, final ValueId id) {
            final var deleteCommand = DeleteCommand.builder()
                    .index(INDEX)
                    .id(id)
                    .nodeId(NodeIds.shard(id.getValue(), id.getValue()))
                    .build();

            assertThrows(
                    IllegalArgumentException.class,
                    () -> deleteCommand.execute(storage),
                    "must throw if entity missing");
        }

        @Test
        @DisplayName("insert and delete entity")
        void testInsertAndDelete(final StorageClient storage, final ValueId id) {
            final var getCommand = GetCommand.forType(EntityHelpers.ENTITY_TYPE)
                    .index(INDEX)
                    .id(id)
                    .build();
            final var entity = ElasticEntity.builder()
                    .id(id.getValue())
                    .name("insert-delete")
                    .count(5)
                    .build();
            final var insertCommand = InsertCommand.withValue(entity)
                    .index(INDEX)
                    .id(id)
                    .build();
            final var deleteCommand = DeleteCommand.builder()
                    .index(INDEX)
                    .id(id)
                    .nodeId(NodeIds.shard(id.getValue(), id.getValue()))
                    .build();

            insertCommand.execute(storage);
            final var entityBefore = getCommand.execute(storage);
            deleteCommand.execute(storage);
            final var entityAfter = getCommand.execute(storage);

            assertAll(
                    "entity",
                    () -> assertTrue(entityBefore.isPresent(), "entity must exist before deletion"),
                    () -> assertEquals(entity, entityBefore.orElse(null), "entity must match inserted value"));
            assertTrue(entityAfter.isEmpty(), "entity must not exist after deletion");
        }

        @Test
        @DisplayName("insert and get projection")
        void testInsertAndGetProjection(final StorageClient storage, final ValueId id) {
            final var entity = ElasticEntity.builder()
                    .id(id.getValue())
                    .name("insert-get-projection")
                    .count(1)
                    .build();
            final var projectedEntity = ElasticEntity.builder()
                    .id(id.getValue())
                    .count(1)
                    .build();
            InsertCommand.withValue(entity)
                    .index(INDEX)
                    .id(id)
                    .build()
                    .execute(storage);

            final var actual = GetCommand.forType(EntityHelpers.ENTITY_TYPE)
                    .index(INDEX)
                    .id(id)
                    .build()
                    .execute(storage);
            final var projected = GetCommand.forType(EntityHelpers.ENTITY_TYPE)
                    .index(INDEX)
                    .id(id)
                    .projection(Projection.builder()
                            .excludeField("name")
                            .build())
                    .build()
                    .execute(storage);

            assertEquals(entity, actual.orElse(null), "entity must match inserted value");
            assertEquals(projectedEntity, projected.orElse(null), "must only have projected fields");
        }

        @BeforeAll
        static void beforeAll(final StorageClient storage) {
            CreateIndexCommand.builder()
                    .index(INDEX)
                    .properties(IndexProperties.builder()
                            .numberOfReplicas(0)
                            .schema(EntityHelpers.SCHEMA)
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

        private static final IndexName INDEX = Indices.name("columbus.entity.commands.test.mock.index");
        private static final ValueId ID = Ids.value("columbus-mock-entity-id");
        private static final ElasticEntity ENTITY = ElasticEntity.builder()
                .id(ID.getValue())
                .name("mock-entity")
                .count(-1)
                .build();

        @Mock(answer = Answers.RETURNS_DEEP_STUBS)
        private ColumbusElasticClient storage;

        @BeforeEach
        void setUp(final Toolkit toolkit) {
            when(storage.toolkit().serDe()).thenReturn(toolkit.serDe());
            when(storage.toolkit().indexResolver().resolve(INDEX).getName()).thenReturn(INDEX.getName());
            when(storage.toolkit().idResolver().resolve(ID).getId()).thenReturn(ID.getValue());
        }

        @Test
        @DisplayName("Insert")
        void testInsertCommand() throws IOException {
            final var command = InsertCommand.withValue(ENTITY)
                    .index(INDEX)
                    .id(ID)
                    .build();

            when(storage.client().index(any(), any())).thenThrow(IOException.class);

            final var handler = new InsertHandler<ElasticEntity, ColumbusElasticClient>();

            assertThrows(IOException.class, () -> handler.handle(command, storage), "must throw on client failure");
        }

        @Test
        @DisplayName("Get")
        void testGetCommand() throws IOException {
            final var command = GetCommand.forType(EntityHelpers.ENTITY_TYPE)
                    .index(INDEX)
                    .id(ID)
                    .build();

            when(storage.client().get(any(), any())).thenThrow(IOException.class);

            final var handler = new GetHandler<ElasticEntity, ColumbusElasticClient>();

            assertThrows(IOException.class, () -> handler.handle(command, storage), "must throw on client failure");
        }

        @Test
        @DisplayName("Upsert")
        void testUpsertCommand() throws IOException {
            final var command = UpsertCommand.withValue(ENTITY)
                    .index(INDEX)
                    .id(ID)
                    .build();

            when(storage.client().update(any(), any())).thenThrow(IOException.class);

            final var handler = new UpsertHandler<ElasticEntity, ColumbusElasticClient>();

            assertThrows(IOException.class, () -> handler.handle(command, storage), "must throw on client failure");
        }

        @Test
        @DisplayName("Delete")
        void testDeleteCommand() throws IOException {
            final var command = DeleteCommand.builder()
                    .index(INDEX)
                    .id(ID)
                    .build();

            when(storage.client().delete(any(), any())).thenThrow(IOException.class);

            final var handler = new DeleteHandler<ColumbusElasticClient>();

            assertThrows(IOException.class, () -> handler.handle(command, storage), "must throw on client failure");
        }
    }
}
