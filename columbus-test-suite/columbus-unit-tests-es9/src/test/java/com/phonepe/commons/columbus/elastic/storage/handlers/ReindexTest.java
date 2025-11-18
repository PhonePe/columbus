package com.phonepe.commons.columbus.elastic.storage.handlers;

import co.elastic.clients.elasticsearch._types.BulkIndexByScrollFailure;
import co.elastic.clients.elasticsearch.core.ReindexRequest;
import co.elastic.clients.elasticsearch.core.ReindexResponse;
import co.elastic.clients.elasticsearch.tasks.GetTasksRequest;
import co.elastic.clients.elasticsearch.tasks.GetTasksResponse;
import co.elastic.clients.elasticsearch.tasks.TaskInfo;
import co.elastic.clients.json.JsonData;
import com.phonepe.commons.columbus.dao.id.Ids;
import com.phonepe.commons.columbus.dao.index.Index;
import com.phonepe.commons.columbus.dao.index.IndexName;
import com.phonepe.commons.columbus.dao.index.Indices;
import com.phonepe.commons.columbus.elastic9.internal.storage.ColumbusElasticClient;
import com.phonepe.commons.columbus.elastic9.internal.storage.handlers.entity.bulk.CopyHandler;
import com.phonepe.commons.columbus.elastic9.internal.storage.handlers.entity.status.GetCopyStatusHandler;
import com.phonepe.commons.columbus.elastic9.storage.toolkit.ElasticToolkit;
import com.phonepe.commons.columbus.search.misc.query.Projection;
import com.phonepe.commons.columbus.search.query.term.TermQuery;
import com.phonepe.commons.columbus.storage.StorageClient;
import com.phonepe.commons.columbus.storage.commands.entity.bulk.CopyCommand;
import com.phonepe.commons.columbus.storage.commands.entity.status.GetCopyStatusCommand;
import com.phonepe.commons.columbus.storage.commands.entity.status.GetCopyStatusCommand.Result;
import com.phonepe.commons.columbus.storage.models.RequestExecutionPolicy;
import com.phonepe.commons.columbus.es9.test.annotations.WithElasticClient;
import com.phonepe.commons.columbus.es9.test.annotations.WithElasticToolkit;
import com.phonepe.commons.columbus.test.annotations.WithMockito;
import com.phonepe.commons.columbus.test.annotations.WithUniqueTestIndex;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Answers;
import org.mockito.Mock;

import java.io.IOException;
import java.io.StringReader;
import java.time.Duration;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static com.phonepe.commons.columbus.elastic.storage.handlers.EntityHelpers.document;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.testcontainers.shaded.org.awaitility.Awaitility.await;

@WithMockito
@WithElasticClient.All
@WithElasticToolkit.All
class ReindexTest {

    private static final IndexName INDEX_1 = Indices.name("columbus.reindex.test.source.v1.index");
    private static final IndexName INDEX_2 = Indices.name("columbus.reindex.test.source.v2.index");
    private static final Map<String, Object> SCHEMA = EntityHelpers.schemaWithAttributeType("keyword");

    private static final List<ElasticEntity> DOCS_1 = IntStream.range(1, 10)
            .mapToObj(index -> document("id_1_" + index, "name_" + index, index, "key_" + (index % 3 + 1)))
            .sorted(Comparator.comparing(ElasticEntity::getId))
            .toList();
    private static final List<ElasticEntity> DOCS_2 = IntStream.range(1, 100)
            .mapToObj(index -> document("id_2_" + index, "name_" + index, index, "key_" + (index % 10 + 1)))
            .sorted(Comparator.comparing(ElasticEntity::getId))
            .toList();

    @BeforeAll
    static void beforeAll(final StorageClient storage) {
        createWithData(storage, INDEX_1, DOCS_1);
        createWithData(storage, INDEX_2, DOCS_2);
    }

    private static void createIndex(final StorageClient storage, final Index index) {
        IndexHelpers.createIndex(storage, index, SCHEMA);
    }

    private static void createWithData(
            final StorageClient storage,
            final Index index,
            final List<ElasticEntity> entities
    ) {
        createIndex(storage, index);
        IndexHelpers.insertEntities(storage, index, entities);
    }

    @Nested
    @WithUniqueTestIndex(format = "columbus.reindex.success.test.v%02d.index")
    class SuccessTest {

        @Test
        @DisplayName("copy single index")
        void testCopySingleIndex(final StorageClient storage, final Index destination) {
            createIndex(storage, destination);
            final var expectedData = DOCS_1;
            final var expectedResult = CopyCommand.Result.builder()
                    .total(expectedData.size())
                    .created(expectedData.size())
                    .build();

            final var command = CopyCommand.builder()
                    .sourceIndex(INDEX_1)
                    .destinationIndex(destination)
                    .build();

            final var result = command.execute(storage);
            // Wait for completion
            await().pollDelay(Duration.ofSeconds(2)).until(() -> true);
            final var copiedDocs = IndexHelpers.readEntities(storage, destination).toList();

            assertAll(
                    "copy single index",
                    () -> assertFalse(result.hasFailures(), "result should have no failures"),
                    () -> assertEquals(expectedResult, result, "copy result should match"),
                    () -> assertEquals(expectedData, copiedDocs, "data should match"));
        }

        @Test
        @DisplayName("copy multiple indices")
        void testCopyMultipleIndices(final StorageClient storage, final Index destination) {
            createIndex(storage, destination);
            final var docsById = Stream.concat(DOCS_1.stream(), DOCS_2.stream())
                    .collect(Collectors.toMap(ElasticEntity::getId, Function.identity()));
            final var maxLimit = 20;
            final var expectedResult = CopyCommand.Result.builder()
                    .total(maxLimit)
                    .created(maxLimit)
                    .build();

            final var command = CopyCommand.builder()
                    .sourceIndex(INDEX_1)
                    .sourceIndex(INDEX_2)
                    .destinationIndex(destination)
                    .maximumCount(maxLimit)
                    // forcing completion for test
                    .executionPolicy(RequestExecutionPolicy.FORCE_COMPLETION)
                    .build();

            final var result = command.execute(storage);
            final var copiedDocs = IndexHelpers.readEntities(storage, destination).toList();

            final var expectedData = copiedDocs.stream()
                    .map(ElasticEntity::getId)
                    .map(docsById::get)
                    .filter(Objects::nonNull)
                    .toList();

            assertAll(
                    "copy multiple indices with limit",
                    () -> assertEquals(expectedResult, result, "copy result should match"),
                    () -> assertEquals(maxLimit, copiedDocs.size(), "should respect maximum limit"),
                    () -> assertEquals(expectedData, copiedDocs, "data should match"));
        }

        @Test
        @DisplayName("copy selected data")
        void testCopySelected(final StorageClient storage, final Index destination) {
            createIndex(storage, destination);
            final var expectedData = DOCS_1.stream()
                    .filter(entity -> "key_3".equals(entity.getAttribute()))
                    .map(entity -> ElasticEntity.builder()
                            .id(entity.getId())
                            .name(entity.getName())
                            .count(0)
                            .attribute(null)
                            .build())
                    .toList();
            final var expectedResult = CopyCommand.Result.builder()
                    .total(expectedData.size())
                    .created(expectedData.size())
                    .build();

            final var command = CopyCommand.builder()
                    .sourceIndex(INDEX_1)
                    .destinationIndex(destination)
                    .query(TermQuery.builder()
                            .field("attribute")
                            .value("key_3")
                            .build())
                    .projection(Projection.builder()
                            .includeField("id")
                            .includeField("name")
                            .build())
                    // forcing completion for test
                    .executionPolicy(RequestExecutionPolicy.FORCE_COMPLETION)
                    .build();

            final var result = command.execute(storage);
            final var copiedDocs = IndexHelpers.readEntities(storage, destination).toList();

            assertAll(
                    "copy selected data",
                    () -> assertEquals(expectedResult, result, "copy result should match"),
                    () -> assertEquals(expectedData, copiedDocs, "data should match"));
        }

        @Test
        @DisplayName("overwrite during copy")
        void testCopyOverwrite(final StorageClient storage, final Index destination) {
            createWithData(storage, destination, DOCS_1.subList(0, 3));
            final var expectedData = DOCS_1;
            final var expectedResult = CopyCommand.Result.builder()
                    .total(expectedData.size())
                    .created(expectedData.size() - 3)
                    .updated(3)
                    .build();

            final var command = CopyCommand.builder()
                    .sourceIndex(INDEX_1)
                    .destinationIndex(destination)
                    // forcing completion for test
                    .executionPolicy(RequestExecutionPolicy.FORCE_COMPLETION)
                    .build();

            final var result = command.execute(storage);
            final var copiedDocs = IndexHelpers.readEntities(storage, destination).toList();

            assertAll(
                    "copy with overwrite",
                    () -> assertEquals(expectedResult, result, "copy result should match"),
                    () -> assertEquals(expectedData, copiedDocs, "data should match"));
        }

        @Test
        @DisplayName("copy index without overwriting")
        void testCopySkipExisting(final StorageClient storage, final Index destination) {
            createWithData(storage, destination, DOCS_1.subList(0, 3));
            final var expectedData = DOCS_1;
            final var expectedResult = CopyCommand.Result.builder()
                    .total(expectedData.size())
                    .created(expectedData.size() - 3)
                    .build();

            final var command = CopyCommand.builder()
                    .sourceIndex(INDEX_1)
                    .destinationIndex(destination)
                    .failOnError(false)
                    .skipExisting(true)
                    // forcing completion for test
                    .executionPolicy(RequestExecutionPolicy.FORCE_COMPLETION)
                    .build();

            final var result = command.execute(storage);
            final var copiedDocs = IndexHelpers.readEntities(storage, destination).toList();

            assertAll(
                    "copy without overwrite",
                    () -> assertEquals(expectedResult, result, "copy result should match"),
                    () -> assertEquals(expectedData, copiedDocs, "data should match"));
        }

        @Test
        @DisplayName("submit copy task")
        void testSubmitCopyTask(final StorageClient storage, final Index destination) {
            createIndex(storage, destination);
            final var expectedData = DOCS_1;

            final var copyCommand = CopyCommand.builder()
                    .sourceIndex(INDEX_1)
                    .destinationIndex(destination)
                    .executionPolicy(RequestExecutionPolicy.SUBMIT)
                    .build();

            final var result = copyCommand.execute(storage);

            final var statusCommand = GetCopyStatusCommand.builder()
                    .taskId(result.getTaskId())
                    .build();

            final var statusDuring = statusCommand.execute(storage);

            // Wait for completion
            await().pollDelay(Duration.ofSeconds(2)).until(() -> true);

            final var statusAfter = statusCommand.execute(storage);

            final var copiedDocs = IndexHelpers.readEntities(storage, destination).toList();

            assertAll(
                    "submit copy task",
                    () -> assertNotNull(result, "result should be valid"),
                    () -> assertFalse(result.hasFailures(), "result should be valid"),
                    () -> assertNotNull(result.getTaskId(), "result should have a task id"),
                    () -> assertFalse(statusDuring.map(Result::isCompleted).orElse(true), "task should be running"),
                    () -> {
                        final var status = statusAfter.orElse(null);
                        assertNotNull(status, "status should be valid");
                        assertAll(
                                "status after execution",
                                () -> assertTrue(status.isCompleted(), "task should be completed"),
                                () -> assertEquals(
                                        expectedData.size(),
                                        status.getTotal(),
                                        "total documents should match"),
                                () -> assertEquals(
                                        expectedData.size(),
                                        status.getCreated(),
                                        "all documents should be created"),
                                () -> assertEquals(0, status.getUpdated(), "no documents should be updated"));
                    },
                    () -> assertEquals(expectedData, copiedDocs, "data should match"));
        }
    }

    @Nested
    @WithUniqueTestIndex(format = "columbus.reindex.invalid.test.v%02d.index")
    class InvalidInputsTest {

        @Test
        @DisplayName("no source indices")
        void testInvalidSourceIndex(final StorageClient storage, final Index destination) {
            final var command = CopyCommand.builder()
                    .destinationIndex(destination)
                    .build();

            assertThrows(IllegalArgumentException.class, () -> command.execute(storage), "invalid sources");
        }

        @Test
        @DisplayName("invalid task id")
        void testInvalidTaskId(final StorageClient storage) {
            final var command = GetCopyStatusCommand.builder()
                    .taskId("unset")
                    .build();

            assertThrows(RuntimeException.class, () -> command.execute(storage), "invalid task id");
        }
    }

    @Nested
    @WithUniqueTestIndex(format = "columbus.reindex.failure.test.v%02d.index")
    class FailureTest {

        @Mock(answer = Answers.RETURNS_DEEP_STUBS)
        private ColumbusElasticClient storage;

        @BeforeEach
        void setUp(final ElasticToolkit toolkit) {
            when(storage.toolkit()).thenReturn(toolkit);
        }

        @Test
        @DisplayName("reindex fails")
        void testReindex(final Index destination) throws IOException {
            final var command = CopyCommand.builder()
                    .sourceIndex(INDEX_1)
                    .destinationIndex(destination)
                    .build();

            when(storage.client().reindex(any(ReindexRequest.class))).thenThrow(IOException.class);

            final var handler = new CopyHandler<ColumbusElasticClient>();

            assertThrows(IOException.class, () -> handler.handle(command, storage), "must throw on client failure");
        }

        @Test
        @DisplayName("submit reindex task fails")
        void testReindexSubmit(final Index destination) throws IOException {
            final var command = CopyCommand.builder()
                    .sourceIndex(INDEX_1)
                    .destinationIndex(destination)
                    .executionPolicy(RequestExecutionPolicy.SUBMIT)
                    .build();

            when(storage.client().reindex(any(ReindexRequest.class))).thenThrow(IOException.class);

            final var handler = new CopyHandler<ColumbusElasticClient>();

            assertThrows(IOException.class, () -> handler.handle(command, storage), "must throw on client failure");
        }

        @Test
        @DisplayName("status of reindex task fails")
        void testReindexStatus() throws IOException {
            final var command = GetCopyStatusCommand.builder()
                    .taskId("elastic:1")
                    .build();

            when(storage.client().tasks().get(any(GetTasksRequest.class))).thenThrow(IOException.class);

            final var handler = new GetCopyStatusHandler<ColumbusElasticClient>();

            assertThrows(IOException.class, () -> handler.handle(command, storage), "must throw on client failure");
        }

        @Test
        @DisplayName("copy index with conflicts")
        void testCopyConflicts(final StorageClient storage, final Index destination) {
            final var existingData = DOCS_1.subList(0, 3);
            createWithData(storage, destination, existingData);
            final var expectedData = DOCS_1;

            final var command = CopyCommand.builder()
                    .sourceIndex(INDEX_1)
                    .destinationIndex(destination)
                    .skipExisting(true)
                    // forcing completion for test
                    .executionPolicy(RequestExecutionPolicy.FORCE_COMPLETION)
                    .build();

            final var result = command.execute(storage);
            final var copiedDocs = IndexHelpers.readEntities(storage, destination).toList();

            assertAll(
                    "copy without overwrite",
                    () -> assertNotNull(result, "result should be valid"),
                    () -> assertTrue(result.hasFailures(), "result should be valid"),
                    () -> assertEquals(expectedData.size(), result.getTotal(), "total documents should match"),
                    () -> assertEquals(
                            expectedData.size() - existingData.size(),
                            result.getCreated(),
                            "only non-conflicting documents should be created"),
                    () -> assertEquals(0, result.getUpdated(), "no documents should be updated"),
                    () -> assertNull(result.getTaskId(), "no task id should be present"),
                    () -> assertTrue(result.getSearchFailures().isEmpty(), "no search failures"),
                    () -> assertEquals(
                            existingData.size(),
                            result.getWriteFailures().size(),
                            "conflicts should fail to write"),
                    () -> assertEquals(expectedData, copiedDocs, "data should match"));
        }
    }

    @Nested
    class SimulatedTest {

        private static final String INDEX_NAME = "index";
        private static final Index DESTINATION = Indices.name(INDEX_NAME);

        @Mock(answer = Answers.RETURNS_DEEP_STUBS)
        private ColumbusElasticClient storage;

        @BeforeEach
        void setUp(final ElasticToolkit toolkit) {
            when(storage.toolkit()).thenReturn(toolkit);
        }

        @Test
        @DisplayName("reindex failures")
        void testReindexFailures() throws IOException {
            final var command = CopyCommand.builder()
                    .sourceIndex(INDEX_1)
                    .destinationIndex(DESTINATION)
                    .build();
            final var writeError = new Exception("write error");

            final var response = new ReindexResponse.Builder()
                    .took(1L)
                    .total(9L)
                    .created(6L)
                    .updated(2L)
                    .failures(List.of(
                            new BulkIndexByScrollFailure.Builder()
                                    .id("1")
                                    .index(INDEX_NAME)
                                    .cause(new co.elastic.clients.elasticsearch._types.ErrorCause.Builder()
                                            .reason(writeError.getMessage())
                                            .type("type_conflict")
                                            .build())
                                    .status(401)
                                    .build()))
                    .build();

            when(storage.client().reindex(any(ReindexRequest.class))).thenReturn(response);

            final var handler = new CopyHandler<ColumbusElasticClient>();
            final var result = handler.handle(command, storage);

            assertAll(
                    "reindex failures",
                    () -> assertEquals(9, result.getTotal(), "total must match"),
                    () -> assertEquals(6, result.getCreated(), "created must match"),
                    () -> assertEquals(2, result.getUpdated(), "updated must match"),
                    () -> assertEquals(1, result.getWriteFailures().size(), "must have one write failure"),
                    () -> {
                        final var failure = result.getWriteFailures().get(0);
                        assertAll(
                                "write failure",
                                () -> assertEquals(Ids.value("1"), failure.getId(), "id must match"),
                                () -> assertEquals(DESTINATION, failure.getIndex(), "index must match"),
                                () -> assertEquals("Type: type_conflict;Reason: write error;", failure.getMessage(),
                                        "exception must match"));
                    });
        }

        @Test
        @DisplayName("task status type mismatch")
        void testTaskStatusTypeMismatch() throws IOException {
            final var command = GetCopyStatusCommand.builder()
                    .taskId("elastic:1")
                    .build();
            final var now = System.currentTimeMillis();
            final var response = new GetTasksResponse.Builder()
                    .completed(true)
                    .task(new TaskInfo.Builder()
                            .action("reindex")
                            .cancellable(false)
                            .id(1L)
                            .node("123")
                            .type("type")
                            .startTimeInMillis(now)
                            .runningTimeInNanos((long) 1e8)
                            .headers(Map.of())
                            .status(JsonData.from(new StringReader("""
                                   {
                                   "created": 6,
                                   "updated": 2,
                                   "total": 9
                                   }
                                    """)))
                            .build())
                    .build();
            final var expected = GetCopyStatusCommand.Result.builder()
                    .completed(true)
                    .startTime(now)
                    .elapsedTime(100)
                    .total(9)
                    .created(6)
                    .updated(2)
                    .build();

            when(storage.client().tasks().get(any(GetTasksRequest.class))).thenReturn(response);

            final var handler = new GetCopyStatusHandler<ColumbusElasticClient>();
            final var result = handler.handle(command, storage);

            assertTrue(result.isPresent(), "result must be valid");
            assertEquals(expected, result.get(), "task status must match");
        }
    }
}
