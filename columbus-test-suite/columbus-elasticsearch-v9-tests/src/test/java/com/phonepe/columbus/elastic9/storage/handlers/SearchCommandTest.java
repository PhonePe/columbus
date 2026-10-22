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

import co.elastic.clients.elasticsearch.core.SearchRequest;
import com.phonepe.columbus.dao.id.Id;
import com.phonepe.columbus.dao.index.Index;
import com.phonepe.columbus.dao.index.IndexName;
import com.phonepe.columbus.dao.index.Indices;
import com.phonepe.columbus.elastic9.internal.storage.ColumbusElasticClient;
import com.phonepe.columbus.elastic9.internal.storage.handlers.search.SearchHandler;
import com.phonepe.columbus.elastic9.storage.toolkit.ElasticToolkit;
import com.phonepe.columbus.elastic9.test.annotations.WithElasticClient;
import com.phonepe.columbus.elastic9.test.annotations.WithElasticToolkit;
import com.phonepe.columbus.search.aggregation.bucket.TermsAggregation;
import com.phonepe.columbus.search.aggregation.bucket.TermsAggregation.Bucket;
import com.phonepe.columbus.search.aggregator.bucket.TermsAggregator;
import com.phonepe.columbus.search.filter.general.EqualsFilter;
import com.phonepe.columbus.search.misc.query.Collapse;
import com.phonepe.columbus.search.misc.query.InnerQuery;
import com.phonepe.columbus.search.misc.query.Projection;
import com.phonepe.columbus.search.misc.result.Hit;
import com.phonepe.columbus.search.misc.result.Result;
import com.phonepe.columbus.search.misc.result.TotalCount;
import com.phonepe.columbus.search.page.Pages;
import com.phonepe.columbus.search.sort.FieldSort;
import com.phonepe.columbus.search.sort.SortOrder;
import com.phonepe.columbus.storage.StorageClient;
import com.phonepe.columbus.storage.commands.search.SearchCommand;
import com.phonepe.columbus.storage.models.result.SearchResult;
import com.phonepe.columbus.test.annotations.WithMockito;
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
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static com.phonepe.columbus.elastic9.storage.handlers.EntityHelpers.document;
import static com.phonepe.columbus.elastic9.storage.handlers.IndexHelpers.deleteIndex;
import static com.phonepe.columbus.elastic9.storage.handlers.IndexHelpers.insertEntities;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WithMockito
@WithElasticClient.All
@WithElasticToolkit.All
class SearchCommandTest {

    private static final IndexName EMPTY_INDEX = Indices.name("columbus.search.command.test.empty.index");
    private static final IndexName DATA_INDEX = Indices.name("columbus.search.command.test.data.index");
    private static final IndexName DUMMY_INDEX = Indices.name("columbus.search.command.test.dummy.index");
    private static final Map<String, Object> SCHEMA = EntityHelpers.schemaWithAttributeType("keyword");

    private static final ElasticEntity[] DOCS = new ElasticEntity[]{
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
    class SuccessTest {

        @Test
        @DisplayName("Search with empty results")
        void testEmptySearch(final StorageClient storage) {
            final var searchCommand = SearchCommand.forType(EntityHelpers.ENTITY_TYPE).index(EMPTY_INDEX).build();

            final var expected = SearchResult.<ElasticEntity>builder()
                    .result(Result.<ElasticEntity>builder()
                            .totalCount(TotalCount.exact(0))
                            .maxScore(null)
                            .build())
                    .build();

            final var result = searchCommand.execute(storage);

            assertEquals(expected, result, "must return empty result");
        }

        @Test
        @DisplayName("Search with no indices")
        void testWithNoIndices(final StorageClient storage) {
            final var searchCommand = SearchCommand.forType(EntityHelpers.ENTITY_TYPE)
                    .indices(null)
                    .indices(List.of())
                    .build();

            assertThrows(
                    IllegalArgumentException.class, () -> searchCommand.execute(storage),
                    "must throw if no indices are provided");
        }

        @Test
        @DisplayName("Search with query, sort and projection")
        void testQuerySortAndProjection(final StorageClient storage) {
            final var searchCommand = SearchCommand.forType(EntityHelpers.ENTITY_ID_TYPE)
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
                    .page(Pages.offset(2, 0))
                    .consistentScoring(true)
                    .build();

            final var response = searchCommand.execute(storage);

            assertNotNull(response, "must return valid result");
            final var result = response.getResult();
            assertEquals(2, result.getHits().size(), "must return only 2 entries");
            verifyIds(result.getHits(), List.of(0, 1));
            assertEquals(TotalCount.exact(3), result.getTotalCount(), "must have exact count");
            assertTrue(response.getAggregations().isEmpty(), "must not have any aggregations");
        }

        @Test
        @DisplayName("Aggregation")
        void testAggregation(final StorageClient storage) {
            final var searchCommand = SearchCommand.forType(EntityHelpers.ENTITY_TYPE)
                    .index(DATA_INDEX)
                    .aggregator(TermsAggregator.builder()
                            .name("terms")
                            .field("attribute")
                            .build())
                    .page(Pages.empty())
                    .build();
            final var counts = Map.<Object, Long>of("key_1", 3L, "key_2", 2L, "key_3", 2L, "key_4", 1L, "key_5", 2L);

            final var response = searchCommand.execute(storage);

            assertNotNull(response, "must return valid result");
            final var result = response.getResult();
            assertTrue(result.getHits().isEmpty(), "must return no results");
            assertEquals(TotalCount.exact(DOCS.length), result.getTotalCount(), "must have exact count");
            assertEquals(1, response.getAggregations().size(), "must have one aggregation");
            final var aggregation = (TermsAggregation) response.getAggregations().get("terms");
            assertEquals("terms", aggregation.getName(), "must match name");
            final var termCounts = aggregation.getBuckets().stream()
                    .collect(Collectors.toMap(Bucket::getKey, Bucket::getCount));
            assertEquals(counts, termCounts, "counts must match");
        }

        @Test
        @DisplayName("Collapse")
        void testCollapse(final StorageClient storage) {
            final var searchCommand = SearchCommand.forType(EntityHelpers.ENTITY_TYPE)
                    .index(DATA_INDEX)
                    .sort(FieldSort.builder().field("attribute").build())
                    .sort(FieldSort.builder().field("count").build())
                    .sort(FieldSort.builder().field("name").build())
                    .collapse(Collapse.builder()
                            .field("attribute")
                            .parallelism(10)
                            .innerQuery(InnerQuery.forType(EntityHelpers.ENTITY_ID_TYPE)
                                    .name("counts")
                                    .size(2)
                                    .sort(FieldSort.builder().field("count").build())
                                    .sort(FieldSort.builder().field("name").build())
                                    .projection(Projection.builder().includeField("id").build())
                                    .innerCollapse(Collapse.builder().field("count").build())
                                    .ignoreInvalid(true)
                                    .build())
                            .build())
                    .build();

            final var response = searchCommand.execute(storage);

            assertNotNull(response, "must return valid result");
            final var result = response.getResult();
            final var hits = result.getHits();
            verifyEntities(hits, List.of(0, 4, 5, 7, 9));
            verifyCollapseHit(hits.get(0), List.of(0, 1, 2));
            verifyCollapseHit(hits.get(1), List.of(4, 3));
            verifyCollapseHit(hits.get(2), List.of(5, 6));
            verifyCollapseHit(hits.get(3), List.of(7));
            verifyCollapseHit(hits.get(4), List.of(9, 8));
            assertEquals(TotalCount.exact(DOCS.length), result.getTotalCount(), "must have exact count");
            assertTrue(response.getAggregations().isEmpty(), "must not have any aggregations");
        }

        @Test
        @DisplayName("Search after pointer")
        void testSearchAfter(final StorageClient storage) {
            final var searchCommand = SearchCommand.forType(EntityHelpers.ENTITY_ID_TYPE)
                    .index(DATA_INDEX)
                    .sort(FieldSort.builder().field("name").build())
                    .projection(Projection.builder().includeField("id").build())
                    .page(Pages.pointer(10, "name_3"))
                    .build();

            final var response = searchCommand.execute(storage);

            assertNotNull(response, "must return valid result");
            final var result = response.getResult();
            assertEquals(1, result.getHits().size(), "must return only one entry");
            verifyIds(result.getHits(), List.of(3));
            assertEquals(TotalCount.exact(DOCS.length), result.getTotalCount(), "must have exact count");
            assertTrue(response.getAggregations().isEmpty(), "must not have any aggregations");
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

        private static <T> void verifyEntities(final List<Hit<T>> hits, final List<Integer> ids, final T[] expected) {
            assertAll(
                    "hits",
                    Stream.concat(
                            Stream.of(() -> assertEquals(ids.size(), hits.size(), "must have exact size")),
                            IntStream.range(0, ids.size()).boxed().flatMap(idx -> {
                                final var id = ids.get(idx);
                                final var hit = hits.get(idx);
                                return Stream.of(
                                        () -> assertEquals(IDS[id], hit.getId(), "must match id"),
                                        () -> assertEquals(expected[id], hit.getEntity(), "must match value"));
                            })));
        }

        private static void verifyEntities(final List<Hit<ElasticEntity>> hits, final List<Integer> ids) {
            verifyEntities(hits, ids, DOCS);
        }

        private static void verifyIds(final List<Hit<ElasticEntityId>> hits, final List<Integer> ids) {
            verifyEntities(hits, ids, ENTITY_IDS);
        }

        private static void verifyCollapseHit(final Hit<ElasticEntity> hit, final List<Integer> ids) {
            final var doc = hit.getEntity();
            assertIterableEquals(
                    List.of(doc.getAttribute(), (long) doc.getCount(), doc.getName()), hit.getSortValues(),
                    "sort values must match");
            assertEquals(1, hit.getInnerResults().size(), "inner results must contain 1 request");
            @SuppressWarnings("unchecked") final var result =
                    (Result<ElasticEntityId>) hit.getInnerResults().get("counts");
            verifyIds(result.getHits(), ids.subList(0, Math.min(ids.size(), 2)));
            assertEquals(TotalCount.exact(ids.size()), result.getTotalCount(), "must have exact count");
        }
    }

    @Nested
    class FailureTest {

        @Mock(answer = Answers.RETURNS_DEEP_STUBS)
        private ColumbusElasticClient storage;

        @BeforeEach
        void setUp(final ElasticToolkit toolkit) {
            when(storage.toolkit()).thenReturn(toolkit);
        }

        @Test
        @DisplayName("Search")
        void testSearchCommand() throws IOException {
            final var command = SearchCommand.forType(EntityHelpers.ENTITY_TYPE)
                    .index(DUMMY_INDEX)
                    .build();

            when(storage.client().search(any(SearchRequest.class), ArgumentMatchers.<Type>any()))
                    .thenThrow(IOException.class);

            final var handler = new SearchHandler<ElasticEntity, ColumbusElasticClient>();

            assertThrows(IOException.class, () -> handler.handle(command, storage), "must throw on client failure");
        }
    }
}
