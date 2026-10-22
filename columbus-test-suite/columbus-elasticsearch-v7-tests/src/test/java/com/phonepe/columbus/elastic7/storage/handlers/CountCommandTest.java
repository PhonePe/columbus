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
package com.phonepe.columbus.elastic7.storage.handlers;

import com.phonepe.columbus.dao.index.Index;
import com.phonepe.columbus.dao.index.IndexName;
import com.phonepe.columbus.dao.index.Indices;
import com.phonepe.columbus.elastic7.internal.storage.ColumbusElasticClient;
import com.phonepe.columbus.elastic7.internal.storage.handlers.search.CountHandler;
import com.phonepe.columbus.elastic7.storage.toolkit.ElasticToolkit;
import com.phonepe.columbus.search.filter.general.EqualsFilter;
import com.phonepe.columbus.search.misc.result.TotalCount;
import com.phonepe.columbus.storage.StorageClient;
import com.phonepe.columbus.storage.commands.search.CountCommand;
import com.phonepe.columbus.elastic7.test.annotations.WithElasticClient;
import com.phonepe.columbus.elastic7.test.annotations.WithElasticToolkit;
import com.phonepe.columbus.test.annotations.WithMockito;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Answers;
import org.mockito.Mock;

import java.io.IOException;
import java.util.Map;

import static com.phonepe.columbus.elastic7.storage.handlers.EntityHelpers.document;
import static com.phonepe.columbus.elastic7.storage.handlers.IndexHelpers.deleteIndex;
import static com.phonepe.columbus.elastic7.storage.handlers.IndexHelpers.insertEntities;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WithMockito
@WithElasticClient.All
@WithElasticToolkit.All
class CountCommandTest {

    private static final IndexName EMPTY_INDEX = Indices.name("columbus.count.command.test.empty.index");
    private static final IndexName DATA_INDEX = Indices.name("columbus.count.command.test.data.index");
    private static final IndexName DUMMY_INDEX = Indices.name("columbus.count.command.test.dummy.index");
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

    private static void createIndex(final StorageClient storage, final Index index) {
        IndexHelpers.createIndex(storage, index, SCHEMA);
    }

    @Nested
    class SuccessTest {

        @Test
        @DisplayName("Count with empty results")
        void testEmptySearch(final StorageClient storage) {
            final var command = CountCommand.builder().index(EMPTY_INDEX).build();

            final var expected = TotalCount.exact(0);

            final var result = command.execute(storage);

            assertEquals(expected, result, "must return 0 count");
        }

        @Test
        @DisplayName("Count with no query")
        void testWithNoQuery(final StorageClient storage) {
            final var command = CountCommand.builder().index(DATA_INDEX).build();

            final var expected = TotalCount.exact(DOCS.length);

            final var result = command.execute(storage);

            assertEquals(expected, result, "must return exact count");
        }

        @Test
        @DisplayName("Count with query")
        void testQuery(final StorageClient storage) {
            final var command = CountCommand.builder()
                    .index(DATA_INDEX)
                    .query(EqualsFilter.builder()
                            .field("attribute")
                            .value("key_1")
                            .build())
                    .threshold(100L)
                    .build();

            final var expected = TotalCount.exact(3);

            final var response = command.execute(storage);

            assertEquals(expected, response, "must have exact count");
        }

        @Test
        @DisplayName("Threshold")
        void testThreshold(final StorageClient storage) {
            final var command = CountCommand.builder()
                    .index(DATA_INDEX)
                    .query(EqualsFilter.builder()
                            .field("attribute")
                            .value("key_1")
                            .build())
                    .threshold(1L)
                    .build();

            final var response = command.execute(storage);

            assertFalse(response.exact(), "must return an approximate count");
            assertTrue(response.getCount() >= 1, "count must be at least threshold");
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
        @DisplayName("Count")
        void testCountCommand() throws IOException {
            final var command = CountCommand.builder()
                    .index(DUMMY_INDEX)
                    .build();

            when(storage.client().count(any(), any())).thenThrow(IOException.class);

            final var handler = new CountHandler<ColumbusElasticClient>();

            assertThrows(IOException.class, () -> handler.handle(command, storage), "must throw on client failure");
        }
    }
}
