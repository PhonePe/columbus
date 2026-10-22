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

import com.phonepe.columbus.core.toolkit.Toolkit;
import com.phonepe.columbus.dao.index.IndexProperties;
import com.phonepe.columbus.elastic7.internal.storage.ColumbusElasticClient;
import com.phonepe.columbus.elastic7.internal.storage.handlers.index.template.CreateIndexTemplateHandler;
import com.phonepe.columbus.elastic7.internal.storage.handlers.index.template.DeleteIndexTemplateHandler;
import com.phonepe.columbus.storage.StorageClient;
import com.phonepe.columbus.storage.commands.index.template.CreateIndexTemplateCommand;
import com.phonepe.columbus.storage.commands.index.template.DeleteIndexTemplateCommand;
import com.phonepe.columbus.elastic7.test.annotations.WithElasticClient;
import com.phonepe.columbus.test.annotations.WithMockito;
import com.phonepe.columbus.test.annotations.WithUniqueString;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.indices.DeleteComposableIndexTemplateRequest;
import org.elasticsearch.client.indices.PutComposableIndexTemplateRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Answers;
import org.mockito.Mock;

import java.io.IOException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WithMockito
@WithElasticClient.All
class IndexTemplateCommandsTest {

    private static final Map<String, Object> SCHEMA =
            Map.of("properties", Map.of("id", Map.of("type", "keyword")));

    @Nested
    @WithUniqueString(format = "columbus.index.template.command.test.v%02d.template")
    class SuccessTest {

        @Test
        @DisplayName("delete non-existent index template")
        void testDeleteNonExistentTemplate(
                final StorageClient storage,
                @WithUniqueString.Inject final String template
        ) {
            final var deleteCommand = DeleteIndexTemplateCommand.builder()
                    .templateName(template)
                    .build();

            assertThrows(
                    IllegalArgumentException.class,
                    () -> deleteCommand.execute(storage),
                    "must throw on deletion failure");
        }

        @Test
        @DisplayName("create and delete index template")
        void testCreateAndDeleteIndexTemplate(
                final StorageClient storage,
                @WithUniqueString.Inject final String template
        ) {

            final var createCommand = CreateIndexTemplateCommand.builder()
                    .templateName(template)
                    .indexPattern("columbus.index.template.command.test.v*.index")
                    .properties(IndexProperties.builder()
                            .schema(SCHEMA)
                            .numberOfShards(1)
                            .numberOfReplicas(1)
                            .build())
                    .build();
            final var deleteCommand = DeleteIndexTemplateCommand.builder()
                    .templateName(template)
                    .build();

            assertDoesNotThrow(() -> createCommand.execute(storage), "create template must succeed");
            assertDoesNotThrow(() -> deleteCommand.execute(storage), "delete template must succeed");
        }
    }

    @Nested
    class FailureTest {

        private static final String TEMPLATE = "columbus.index.template.commands.test.mock.template";

        @Mock(answer = Answers.RETURNS_DEEP_STUBS)
        private ColumbusElasticClient storage;

        @BeforeEach
        void setUp(final Toolkit toolkit) {
            when(storage.toolkit().serDe()).thenReturn(toolkit.serDe());
        }

        @Test
        @DisplayName("create not acknowledged")
        void testCreateIndexTemplateNotAcknowledged() throws IOException {
            final var command = CreateIndexTemplateCommand.builder()
                    .templateName(TEMPLATE)
                    .properties(IndexProperties.builder().build())
                    .build();

            when(storage.client().indices().putIndexTemplate(any(PutComposableIndexTemplateRequest.class), any()))
                    .thenReturn(new AcknowledgedResponse(false));

            final var handler = new CreateIndexTemplateHandler<ColumbusElasticClient>();

            assertDoesNotThrow(
                    () -> handler.handle(command, storage),
                    "must succeed delayed ack");
        }

        @Test
        @DisplayName("create failed")
        void testCreateIndexTemplateFailed() throws IOException {
            final var command = CreateIndexTemplateCommand.builder()
                    .templateName(TEMPLATE)
                    .properties(IndexProperties.builder().build())
                    .build();

            when(storage.client().indices().putIndexTemplate(any(PutComposableIndexTemplateRequest.class), any()))
                    .thenThrow(IOException.class);

            final var handler = new CreateIndexTemplateHandler<ColumbusElasticClient>();

            assertThrows(
                    IOException.class,
                    () -> handler.handle(command, storage),
                    "must throw on client failure");
        }

        @Test
        @DisplayName("delete not acknowledged")
        void testDeleteIndexTemplateNotAcknowledged() throws IOException {
            final var command = DeleteIndexTemplateCommand.builder()
                    .templateName(TEMPLATE)
                    .build();

            when(storage.client().indices().deleteIndexTemplate(any(DeleteComposableIndexTemplateRequest.class), any()))
                    .thenReturn(new AcknowledgedResponse(false));

            final var handler = new DeleteIndexTemplateHandler<ColumbusElasticClient>();

            assertDoesNotThrow(
                    () -> handler.handle(command, storage),
                    "must succeed delayed ack");
        }

        @Test
        @DisplayName("delete failed")
        void testDeleteIndexTemplateFailed() throws IOException {
            final var command = DeleteIndexTemplateCommand.builder()
                    .templateName(TEMPLATE)
                    .build();

            when(storage.client().indices().deleteIndexTemplate(any(DeleteComposableIndexTemplateRequest.class), any()))
                    .thenThrow(IOException.class);

            final var handler = new DeleteIndexTemplateHandler<ColumbusElasticClient>();

            assertThrows(
                    IOException.class,
                    () -> handler.handle(command, storage),
                    "must throw on client failure");
        }
    }
}
