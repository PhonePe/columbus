package com.phonepe.commons.columbus.elastic.storage.handlers;

import com.phonepe.commons.columbus.dao.id.Id;
import com.phonepe.commons.columbus.dao.index.Index;
import com.phonepe.commons.columbus.dao.index.IndexName;
import com.phonepe.commons.columbus.dao.index.Indices;
import com.phonepe.commons.columbus.elastic.internal.storage.ColumbusElasticClient;
import com.phonepe.commons.columbus.elastic.internal.storage.handlers.search.ScanHandler;
import com.phonepe.commons.columbus.elastic.storage.toolkit.ElasticToolkit;
import com.phonepe.commons.columbus.search.filter.general.EqualsFilter;
import com.phonepe.commons.columbus.search.misc.query.Projection;
import com.phonepe.commons.columbus.search.sort.FieldSort;
import com.phonepe.commons.columbus.search.sort.SortOrder;
import com.phonepe.commons.columbus.storage.StorageClient;
import com.phonepe.commons.columbus.storage.commands.search.ScanCommand;
import com.phonepe.commons.columbus.storage.models.result.StoredEntity;
import com.phonepe.commons.columbus.test.annotations.WithElasticClient;
import com.phonepe.commons.columbus.test.annotations.WithElasticToolkit;
import com.phonepe.commons.columbus.test.annotations.WithMockito;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.bytes.BytesArray;
import org.elasticsearch.search.SearchHit;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Answers;
import org.mockito.Mock;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static com.phonepe.commons.columbus.elastic.storage.handlers.EntityHelpers.document;
import static com.phonepe.commons.columbus.elastic.storage.handlers.IndexHelpers.deleteIndex;
import static com.phonepe.commons.columbus.elastic.storage.handlers.IndexHelpers.insertEntities;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WithMockito
@WithElasticToolkit.All
class ScanCommandTest {

    private static final IndexName EMPTY_INDEX = Indices.name("columbus.scan.command.test.empty.index");
    private static final IndexName DATA_INDEX = Indices.name("columbus.scan.command.test.data.index");
    private static final IndexName DUMMY_INDEX = Indices.name("columbus.scan.command.test.dummy.index");
    private static final Map<String, Object> SCHEMA = EntityHelpers.schemaWithAttributeType("keyword");

    private static final ElasticEntity[] DOCS = {
            document("doc_00", "name_1", 1, "key_1"),
            document("doc_01", "name_2", 2, "key_1"),
            document("doc_02", "name_3", 1, "key_1"),
            document("doc_03", "name_4", 3, "key_2"),
            document("doc_04", "name_1", 1, "key_2"),
            document("doc_05", "name_2", 2, "key_3"),
            document("doc_06", "name_3", 3, "key_3"),
            document("doc_07", "name_1", 1, "key_4"),
            document("doc_08", "name_2", 2, "key_5"),
            document("doc_09", "name_3", 1, "key_5"),
    };

    private static final Id[] IDS = Arrays.stream(DOCS)
            .map(EntityHelpers::id)
            .toArray(Id[]::new);
    private static final ElasticEntityId[] ENTITY_IDS = Arrays.stream(DOCS)
            .map(EntityHelpers::entityId)
            .toArray(ElasticEntityId[]::new);

    private static void createIndex(final StorageClient storage, final Index index) {
        IndexHelpers.createIndex(storage, index, SCHEMA);
    }

    @Nested
    @WithElasticClient.All
    class SuccessTest {

        @Test
        @DisplayName("Scan with empty results")
        void testEmptyScan(final StorageClient storage) {
            final var scanCommand = ScanCommand.forType(EntityHelpers.ENTITY_TYPE)
                    .index(EMPTY_INDEX)
                    .build();

            final var scanner = scanCommand.execute(storage);

            assertNotNull(scanner, "must return a valid scanner");
            assertAll(
                    "empty results",
                    () -> assertFalse(scanner.iterate().hasNext(), "iterator returns no results"),
                    () -> assertFalse(scanner.iterateBatch(10).hasNext(), "iterator returns no results"),
                    () -> assertTrue(scanner.stream().findAny().isEmpty(), "stream returns no results"),
                    () -> assertTrue(scanner.streamBatch(20).findAny().isEmpty(), "stream returns no results"));
        }

        @Test
        @DisplayName("Scan with no indices")
        void testWithNoIndices(final StorageClient storage) {
            final var scanCommand = ScanCommand.forType(EntityHelpers.ENTITY_TYPE)
                    .indices(null)
                    .indices(List.of())
                    .build();

            assertThrows(IllegalArgumentException.class, () -> scanCommand.execute(storage),
                    "must throw if no indices are provided");
        }

        @Test
        @DisplayName("Scan all")
        void testScanAll(final StorageClient storage) {
            final var scanCommand = ScanCommand.forType(EntityHelpers.ENTITY_TYPE)
                    .index(DATA_INDEX)
                    .sort(FieldSort.builder()
                            .order(SortOrder.ASC)
                            .field("id")
                            .build())
                    .batchSize(2)
                    .build();
            final var expectedIds = IntStream.range(0, 10).boxed().toList();

            final var scanner = scanCommand.execute(storage);

            assertNotNull(scanner, "must return a valid scanner");

            final var iterator = scanner.iterate();
            for (int id : expectedIds) {
                assertTrue(iterator.hasNext(), "must have more entries");
                final var value = iterator.next();
                assertEquals(IDS[id], value.getId(), "id must match");
                assertEquals(DOCS[id], value.getValue(), "value must match");
            }
            assertFalse(iterator.hasNext(), "no more entries left");

            final var batchIterator = scanner.iterateBatch(6);
            assertTrue(batchIterator.hasNext(), "must have entries");
            verifyEntities(batchIterator.next(), expectedIds.subList(0, 6));
            assertTrue(batchIterator.hasNext(), "must have entries");
            verifyEntities(batchIterator.next(), expectedIds.subList(6, 10));
            assertFalse(batchIterator.hasNext(), "no more entries left");

            verifyEntities(scanner.stream().toList(), expectedIds);
            verifyEntities(scanner.streamBatch(10).findFirst().orElse(null), expectedIds);
        }

        @Test
        @DisplayName("Scan with query, sort and projection")
        void testQuerySortAndProjection(final StorageClient storage) {
            final var scanCommand = ScanCommand.forType(EntityHelpers.ENTITY_ID_TYPE)
                    .index(DATA_INDEX)
                    .query(EqualsFilter.builder()
                            .field("attribute")
                            .value("key_1")
                            .build())
                    .sort(FieldSort.builder()
                            .order(SortOrder.ASC)
                            .field("name")
                            .build())
                    .projection(Projection.builder()
                            .includeField("id")
                            .build())
                    .batchSize(5)
                    .build();
            final var expectedIds = List.of(0, 1, 2);

            final var scanner = scanCommand.execute(storage);

            assertNotNull(scanner, "must return a valid scanner");

            final var iterator = scanner.iterate();
            for (int id : expectedIds) {
                assertTrue(iterator.hasNext(), "must have more entries");
                final var value = iterator.next();
                assertEquals(IDS[id], value.getId(), "id must match");
                assertEquals(ENTITY_IDS[id], value.getValue(), "value must match");
            }
            assertFalse(iterator.hasNext(), "no more entries left");

            final var batchIterator = scanner.iterateBatch(5);
            assertTrue(batchIterator.hasNext(), "must have entries");
            verifyIds(batchIterator.next(), expectedIds);
            assertFalse(batchIterator.hasNext(), "no more entries left");
            assertThrows(NoSuchElementException.class, batchIterator::next, "next() throws if no entry left");

            verifyIds(scanner.stream().toList(), expectedIds);
            verifyIds(scanner.streamBatch(10).findFirst().orElse(null), expectedIds);
        }

        @BeforeAll
        static void beforeAll(final StorageClient storage) {
            createIndex(storage, DATA_INDEX);
            insertEntities(storage, DATA_INDEX, DOCS);
            createIndex(storage, EMPTY_INDEX);
        }

        @AfterAll
        static void afterAll(final StorageClient storage) {
            deleteIndex(storage, DATA_INDEX);
            deleteIndex(storage, EMPTY_INDEX);
        }

        private static void verifyEntities(final List<StoredEntity<ElasticEntity>> entities, final List<Integer> ids) {
            verifyEntities(entities, ids, DOCS);
        }

        private static void verifyIds(final List<StoredEntity<ElasticEntityId>> entities, final List<Integer> ids) {
            verifyEntities(entities, ids, ENTITY_IDS);
        }

        private static <T> void verifyEntities(
                final List<StoredEntity<T>> entities,
                final List<Integer> ids,
                final T[] expected
        ) {
            assertAll(
                    "entities",
                    Stream.concat(
                            Stream.of(() -> assertEquals(ids.size(), entities.size(), "must have exact size")),
                            IntStream.range(0, ids.size()).boxed().flatMap(idx -> {
                                final var id = ids.get(idx);
                                final var entity = entities.get(idx);
                                return Stream.of(
                                        () -> assertEquals(IDS[id], entity.getId(), "must match id"),
                                        () -> assertEquals(expected[id], entity.getValue(), "must match value"));
                            })));
        }
    }

    @Nested
    class FailureTest {

        @Mock(answer = Answers.RETURNS_DEEP_STUBS)
        private ColumbusElasticClient storage;

        @Mock(answer = Answers.RETURNS_DEEP_STUBS)
        private SearchResponse elasticResponse;

        @BeforeEach
        void setUp(final ElasticToolkit toolkit) {
            when(storage.toolkit()).thenReturn(toolkit);
        }

        @Test
        @DisplayName("Scan Search fails")
        void testScanSearch() throws IOException {
            final var command = ScanCommand.forType(EntityHelpers.ENTITY_TYPE)
                    .index(DUMMY_INDEX)
                    .build();

            when(storage.client().search(any(), any())).thenThrow(IOException.class);

            final var handler = new ScanHandler<ElasticEntity, ColumbusElasticClient>();
            final var scanner = handler.handle(command, storage);

            assertThrows(IOException.class, () -> scanner.stream().toList(), "must throw on client failure");
        }

        @Test
        @DisplayName("Scan Scroll fails")
        void testScanScroll() throws IOException {
            final var command = ScanCommand.forType(EntityHelpers.ENTITY_TYPE)
                    .index(DUMMY_INDEX)
                    .batchSize(1)
                    .build();

            final var hit = SearchHit.createFromMap(Map.of(
                    "_id", "id",
                    "_score", 1f,
                    "_source", new BytesArray("{\"id\": \"id\"}")));
            when(elasticResponse.getHits().getHits()).thenReturn(new SearchHit[]{hit});
            when(storage.client().search(any(), any())).thenReturn(elasticResponse);
            when(storage.client().scroll(any(), any())).thenThrow(IOException.class);

            final var handler = new ScanHandler<ElasticEntity, ColumbusElasticClient>();
            final var scanner = handler.handle(command, storage);

            assertThrows(IOException.class, () -> scanner.stream().toList(), "must throw on client failure");
        }
    }
}
