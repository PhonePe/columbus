package com.phonepe.columbus.elastic9.storage.handlers;

import co.elastic.clients.elasticsearch.cluster.HealthRequest;
import co.elastic.clients.elasticsearch.indices.CreateIndexRequest;
import co.elastic.clients.elasticsearch.indices.CreateIndexResponse;
import co.elastic.clients.elasticsearch.indices.DeleteIndexRequest;
import co.elastic.clients.elasticsearch.indices.DeleteIndexResponse;
import co.elastic.clients.elasticsearch.indices.ExistsRequest;
import co.elastic.clients.elasticsearch.indices.GetIndexRequest;
import co.elastic.clients.elasticsearch.indices.GetIndexResponse;
import co.elastic.clients.elasticsearch.indices.IndexState;
import co.elastic.clients.elasticsearch.indices.PutIndicesSettingsRequest;
import co.elastic.clients.elasticsearch.indices.PutMappingRequest;
import co.elastic.clients.elasticsearch.indices.PutMappingResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.phonepe.columbus.dao.index.Index;
import com.phonepe.columbus.dao.index.IndexName;
import com.phonepe.columbus.dao.index.IndexProperties;
import com.phonepe.columbus.dao.index.Indices;
import com.phonepe.columbus.elastic9.internal.storage.ColumbusElasticClient;
import com.phonepe.columbus.elastic9.internal.storage.handlers.index.CreateIndexHandler;
import com.phonepe.columbus.elastic9.internal.storage.handlers.index.DeleteIndexHandler;
import com.phonepe.columbus.elastic9.internal.storage.handlers.index.GetIndexHandler;
import com.phonepe.columbus.elastic9.internal.storage.handlers.index.IndexExistsHandler;
import com.phonepe.columbus.elastic9.internal.storage.handlers.index.UpdateIndexHandler;
import com.phonepe.columbus.elastic9.test.annotations.WithElasticClient;
import com.phonepe.columbus.serde.SerDes;
import com.phonepe.columbus.storage.StorageClient;
import com.phonepe.columbus.storage.commands.index.CreateIndexCommand;
import com.phonepe.columbus.storage.commands.index.DeleteIndexCommand;
import com.phonepe.columbus.storage.commands.index.GetIndexCommand;
import com.phonepe.columbus.storage.commands.index.IndexExistsCommand;
import com.phonepe.columbus.storage.commands.index.UpdateIndexCommand;
import com.phonepe.columbus.test.annotations.WithMockito;
import com.phonepe.columbus.test.annotations.WithUniqueTestIndex;
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
                    () -> assertEquals(
                            "30s", properties.getSettings().get("refresh_interval"),
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
                    () -> assertEquals(
                            "30s", properties.getSettings().get("refresh_interval"),
                            "settings must be applied"));

            assertAll(
                    "updated properties",
                    () -> assertNotNull(updatedProperties, "must return valid properties"),
                    () -> assertEquals(1, updatedProperties.getNumberOfShards(), "shards must match"),
                    () -> assertEquals(0, updatedProperties.getNumberOfReplicas(), "replicas must match"),
                    () -> assertEquals(newSchema, updatedProperties.getSchema(), "schema must be updated"),
                    () -> assertNotNull(updatedProperties.getSettings(), "must return valid settings"),
                    () -> assertEquals(
                            "10s", updatedProperties.getSettings().get("refresh_interval"),
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
            when(storage.toolkit().serDe()).thenReturn(SerDes.json(new ObjectMapper()));
        }

        @Test
        @DisplayName("Create")
        void testCreateIndexCommand() throws IOException {
            final var command = CreateIndexCommand.builder()
                    .index(INDEX)
                    .properties(IndexProperties.builder().build())
                    .build();

            when(storage.client().indices().create(any(CreateIndexRequest.class)))
                    .thenReturn(new CreateIndexResponse.Builder()
                            .index(INDEX.getName())
                            .acknowledged(false)
                            .shardsAcknowledged(false)
                            .build());
            when(storage.client().cluster().health(any(HealthRequest.class))).thenThrow(IOException.class);

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

            when(storage.client().indices().exists(any(ExistsRequest.class))).thenThrow(IOException.class);

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

            when(storage.client().indices().get(any(GetIndexRequest.class))).thenThrow(IOException.class);

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

            when(storage.client().indices().putMapping(any(PutMappingRequest.class)))
                    .thenReturn(new PutMappingResponse.Builder().acknowledged(false).build());
            when(storage.client().indices().putSettings(any(PutIndicesSettingsRequest.class)))
                    .thenThrow(IOException.class);

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

            when(storage.client().indices().delete(any(DeleteIndexRequest.class))).thenThrow(IOException.class);

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

            when(storage.client().indices().delete(any(DeleteIndexRequest.class))).thenReturn(
                    new DeleteIndexResponse.Builder().acknowledged(false).build());

            final var handler = new DeleteIndexHandler<ColumbusElasticClient>();
            final var delete = handler.handle(command, storage);

            assertFalse(delete, "ack must be returned");
        }

        @Test
        @DisplayName("Get - null index state")
        void testGetIndexCommandNullState() throws IOException {
            final var command = GetIndexCommand.builder()
                    .index(INDEX)
                    .build();

            final var response = new GetIndexResponse.Builder()
                    .indices(Map.of())
                    .build();
            when(storage.client().indices().get(any(GetIndexRequest.class))).thenReturn(response);

            final var handler = new GetIndexHandler<ColumbusElasticClient>();

            assertThrows(
                    IllegalStateException.class,
                    () -> handler.handle(command, storage),
                    "must throw when index state is null");
        }

        @Test
        @DisplayName("Get - null mappings")
        void testGetIndexCommandNullMappings() throws IOException {
            final var command = GetIndexCommand.builder()
                    .index(INDEX)
                    .build();

            final var response = new GetIndexResponse.Builder()
                    .indices(INDEX.getName(), new IndexState.Builder().build())
                    .build();
            when(storage.client().indices().get(any(GetIndexRequest.class))).thenReturn(response);

            final var handler = new GetIndexHandler<ColumbusElasticClient>();

            assertThrows(
                    IllegalStateException.class,
                    () -> handler.handle(command, storage),
                    "must throw when mappings are null");
        }
    }
}
