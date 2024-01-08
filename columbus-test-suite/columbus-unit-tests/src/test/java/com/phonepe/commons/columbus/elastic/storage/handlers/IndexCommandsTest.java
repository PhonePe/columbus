package com.phonepe.commons.columbus.elastic.storage.handlers;

import com.phonepe.commons.columbus.dao.index.Index;
import com.phonepe.commons.columbus.dao.index.IndexName;
import com.phonepe.commons.columbus.dao.index.IndexProperties;
import com.phonepe.commons.columbus.dao.index.Indices;
import com.phonepe.commons.columbus.elastic.internal.storage.ColumbusElasticClient;
import com.phonepe.commons.columbus.elastic.internal.storage.handlers.index.CreateIndexHandler;
import com.phonepe.commons.columbus.elastic.internal.storage.handlers.index.DeleteIndexHandler;
import com.phonepe.commons.columbus.elastic.internal.storage.handlers.index.GetIndexHandler;
import com.phonepe.commons.columbus.elastic.internal.storage.handlers.index.IndexExistsHandler;
import com.phonepe.commons.columbus.elastic.internal.storage.handlers.index.UpdateIndexHandler;
import com.phonepe.commons.columbus.storage.StorageClient;
import com.phonepe.commons.columbus.storage.commands.index.CreateIndexCommand;
import com.phonepe.commons.columbus.storage.commands.index.DeleteIndexCommand;
import com.phonepe.commons.columbus.storage.commands.index.GetIndexCommand;
import com.phonepe.commons.columbus.storage.commands.index.IndexExistsCommand;
import com.phonepe.commons.columbus.storage.commands.index.UpdateIndexCommand;
import com.phonepe.commons.columbus.test.annotations.WithElasticClient;
import com.phonepe.commons.columbus.test.annotations.WithMockito;
import com.phonepe.commons.columbus.test.annotations.WithUniqueTestIndex;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.client.indices.PutMappingRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Answers;
import org.mockito.Mock;

import java.io.IOException;
import java.util.Map;

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
class IndexCommandsTest {

    private static final Map<String, Object> SCHEMA =
            Map.of("properties", Map.of("id", Map.of("type", "keyword")));

    @Nested
    @WithUniqueTestIndex(format = "columbus.index.commands.test.v%02d.index")
    class SuccessTest {

        @Test
        @DisplayName("check non-existent index")
        void testIndexExistsMissing(final StorageClient storage, final Index index) {
            final var exists = IndexExistsCommand.builder()
                    .index(index)
                    .build()
                    .execute(storage);

            assertFalse(exists, "index must not exist");
        }

        @Test
        @DisplayName("create and check index exists")
        void testCreateIndexAndCheckExists(final StorageClient storage, final Index index) {
            final var existsCommand = IndexExistsCommand.builder()
                    .index(index)
                    .build();
            final var createCommand = CreateIndexCommand.builder()
                    .index(index)
                    .properties(IndexProperties.builder()
                            .numberOfShards(1)
                            .numberOfReplicas(0)
                            .schema(SCHEMA)
                            .settings(Map.of("refresh_interval", "30s"))
                            .build())
                    .build();

            final var existsBefore = existsCommand.execute(storage);
            createCommand.execute(storage);
            final var existsAfter = existsCommand.execute(storage);

            assertFalse(existsBefore, "index must not exist before creation");
            assertTrue(existsAfter, "index must exist after creation");
        }

        @Test
        @DisplayName("create and get index")
        void testCreateAndGetIndex(final StorageClient storage, final Index index) {
            final var getCommand = GetIndexCommand.builder()
                    .index(index)
                    .build();
            final var createCommand = CreateIndexCommand.builder()
                    .index(index)
                    .properties(IndexProperties.builder()
                            .numberOfShards(1)
                            .numberOfReplicas(0)
                            .schema(SCHEMA)
                            .settings(Map.of("refresh_interval", "30s"))
                            .build())
                    .build();

            createCommand.execute(storage);
            final var properties = getCommand.execute(storage);

            assertAll(
                    "properties",
                    () -> assertNotNull(properties, "must return valid properties"),
                    () -> assertEquals(1, properties.getNumberOfShards(), "shards must match"),
                    () -> assertEquals(0, properties.getNumberOfReplicas(), "replicas must match"),
                    () -> assertEquals(SCHEMA, properties.getSchema(), "schema must match"),
                    () -> assertNotNull(properties.getSettings(), "must return valid settings"),
                    () -> assertEquals("30s", properties.getSettings().get("refresh_interval"),
                            "settings must be applied"));
        }

        @Test
        @DisplayName("create and update index")
        void testCreateAndUpdateIndex(final StorageClient storage, final Index index) {
            final var getCommand = GetIndexCommand.builder()
                    .index(index)
                    .build();
            final var createCommand = CreateIndexCommand.builder()
                    .index(index)
                    .properties(IndexProperties.builder()
                            .numberOfShards(1)
                            .numberOfReplicas(0)
                            .schema(SCHEMA)
                            .settings(Map.of("refresh_interval", "30s"))
                            .build())
                    .build();
            final var newSchema = Map.<String, Object>of("properties", Map.of(
                    "id", Map.of("type", "keyword"),
                    "name", Map.of("type", "keyword")));
            final var updateCommand = UpdateIndexCommand.builder()
                    .index(index)
                    .properties(IndexProperties.builder()
                            .schema(newSchema)
                            .settings(Map.of("refresh_interval", "10s"))
                            .build())
                    .build();

            createCommand.execute(storage);
            final var properties = getCommand.execute(storage);
            updateCommand.execute(storage);
            final var updatedProperties = getCommand.execute(storage);

            assertAll(
                    "properties",
                    () -> assertNotNull(properties, "must return valid properties"),
                    () -> assertEquals(1, properties.getNumberOfShards(), "shards must match"),
                    () -> assertEquals(0, properties.getNumberOfReplicas(), "replicas must match"),
                    () -> assertEquals(SCHEMA, properties.getSchema(), "schema must match"),
                    () -> assertNotNull(properties.getSettings(), "must return valid settings"),
                    () -> assertEquals("30s", properties.getSettings().get("refresh_interval"),
                            "settings must be applied"));

            assertAll(
                    "updated properties",
                    () -> assertNotNull(updatedProperties, "must return valid properties"),
                    () -> assertEquals(1, updatedProperties.getNumberOfShards(), "shards must match"),
                    () -> assertEquals(0, updatedProperties.getNumberOfReplicas(), "replicas must match"),
                    () -> assertEquals(newSchema, updatedProperties.getSchema(), "schema must be updated"),
                    () -> assertNotNull(updatedProperties.getSettings(), "must return valid settings"),
                    () -> assertEquals("10s", updatedProperties.getSettings().get("refresh_interval"),
                            "settings must be updated"));
        }

        @Test
        @DisplayName("create and delete index")
        void testCreateAndDeleteIndex(final StorageClient storage, final Index index) {
            final var existsCommand = IndexExistsCommand.builder()
                    .index(index)
                    .build();
            final var createCommand = CreateIndexCommand.builder()
                    .index(index)
                    .properties(IndexProperties.builder()
                            .numberOfShards(1)
                            .numberOfReplicas(0)
                            .schema(SCHEMA)
                            .build())
                    .build();
            final var deleteCommand = DeleteIndexCommand.builder()
                    .index(index)
                    .build();

            createCommand.execute(storage);
            final var existsBefore = existsCommand.execute(storage);
            final var delete = deleteCommand.execute(storage);
            final var existsAfter = existsCommand.execute(storage);

            assertTrue(existsBefore, "index must exist before deletion");
            assertTrue(delete, "deletion must succeed");
            assertFalse(existsAfter, "index must not exist after deletion");
        }
    }

    @Nested
    class FailureTest {

        private static final IndexName INDEX = Indices.name("columbus.storage.commands.test.mock.index");

        @Mock(answer = Answers.RETURNS_DEEP_STUBS)
        private ColumbusElasticClient storage;

        @BeforeEach
        void setUp() {
            when(storage.toolkit().indexResolver().resolve(INDEX).getName()).thenReturn(INDEX.getName());
        }

        @Test
        @DisplayName("Create")
        void testCreateIndexCommand() throws IOException {
            final var command = CreateIndexCommand.builder()
                    .index(INDEX)
                    .properties(IndexProperties.builder().build())
                    .build();

            when(storage.client().indices().create(any(CreateIndexRequest.class), any()))
                    .thenReturn(new CreateIndexResponse(false, false, INDEX.getName()));
            when(storage.client().cluster().health(any(), any())).thenThrow(IOException.class);

            final var handler = new CreateIndexHandler<ColumbusElasticClient>();

            assertThrows(
                    IOException.class,
                    () -> handler.handle(command, storage),
                    "must throw on client failure");
        }

        @Test
        @DisplayName("Exists")
        void testIndexExistsCommand() throws IOException {
            final var command = IndexExistsCommand.builder()
                    .index(INDEX)
                    .build();

            when(storage.client().indices().exists(any(GetIndexRequest.class), any())).thenThrow(IOException.class);

            final var handler = new IndexExistsHandler<ColumbusElasticClient>();

            assertThrows(
                    IOException.class,
                    () -> handler.handle(command, storage),
                    "must throw on client failure");
        }

        @Test
        @DisplayName("Get")
        void testGetIndexCommand() throws IOException {
            final var command = GetIndexCommand.builder()
                    .index(INDEX)
                    .build();

            when(storage.client().indices().get(any(GetIndexRequest.class), any())).thenThrow(IOException.class);

            final var handler = new GetIndexHandler<ColumbusElasticClient>();

            assertThrows(
                    IOException.class,
                    () -> handler.handle(command, storage),
                    "must throw on client failure");
        }

        @Test
        @DisplayName("Update")
        void testUpdateIndexCommand() throws IOException {
            final var command = UpdateIndexCommand.builder()
                    .index(INDEX)
                    .properties(IndexProperties.builder()
                            .schema(SCHEMA)
                            .settings(Map.of("refresh_interval", "10s"))
                            .build())
                    .build();

            when(storage.client().indices().putMapping(any(PutMappingRequest.class), any()))
                    .thenReturn(new AcknowledgedResponse(false));
            when(storage.client().indices().putSettings(any(), any())).thenThrow(IOException.class);

            final var handler = new UpdateIndexHandler<ColumbusElasticClient>();

            assertThrows(
                    IOException.class,
                    () -> handler.handle(command, storage),
                    "must throw on client failure");
        }

        @Test
        @DisplayName("Delete")
        void testDeleteIndexCommand() throws IOException {
            final var command = DeleteIndexCommand.builder()
                    .index(INDEX)
                    .build();

            when(storage.client().indices().delete(any(), any())).thenThrow(IOException.class);

            final var handler = new DeleteIndexHandler<ColumbusElasticClient>();

            assertThrows(
                    IOException.class,
                    () -> handler.handle(command, storage),
                    "must throw on client failure");
        }

        @Test
        @DisplayName("Delete not acknowledged")
        void testDeleteIndexCommandNoAck() throws IOException {
            final var command = DeleteIndexCommand.builder()
                    .index(INDEX)
                    .build();

            when(storage.client().indices().delete(any(), any())).thenReturn(new AcknowledgedResponse(false));

            final var handler = new DeleteIndexHandler<ColumbusElasticClient>();
            final var delete = handler.handle(command, storage);

            assertFalse(delete, "ack must be returned");
        }
    }
}
