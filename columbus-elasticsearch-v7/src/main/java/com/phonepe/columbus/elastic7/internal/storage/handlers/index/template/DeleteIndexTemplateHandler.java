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
package com.phonepe.columbus.elastic7.internal.storage.handlers.index.template;

import com.phonepe.columbus.elastic7.common.Constants.Operations;
import com.phonepe.columbus.elastic7.storage.ElasticClient;
import com.phonepe.columbus.elastic7.storage.handlers.ElasticStorageCommandHandler;
import com.phonepe.columbus.registry.factory.RegistryKey;
import com.phonepe.columbus.storage.commands.index.template.DeleteIndexTemplateCommand;
import io.appform.functionmetrics.MonitoredFunction;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.ElasticsearchStatusException;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.indices.DeleteComposableIndexTemplateRequest;

import java.io.IOException;

/**
 * Delete an index template in the ElasticSearch cluster.
 *
 * @param <E> the type of the Elastic client.
 */
@Slf4j
@RegistryKey(name = DeleteIndexTemplateCommand.COMMAND_NAME, type = DeleteIndexTemplateCommand.class)
public class DeleteIndexTemplateHandler<E extends ElasticClient<E>>
        implements ElasticStorageCommandHandler<Void, DeleteIndexTemplateCommand, E> {

    @Override
    @SneakyThrows
    public Void handle(final DeleteIndexTemplateCommand command, final E storage) {
        final var templateName = command.getTemplateName();

        final var request = new DeleteComposableIndexTemplateRequest(templateName);

        try {
            final var ack = deleteIndexTemplate(storage, request).isAcknowledged();
            if (!ack) {
                log.warn("Deletion of index template not acknowledged for template: '{}'", templateName);
            }
        } catch (ElasticsearchStatusException e) {
            log.error("Failed to perform deletion, likely the template '{}' is missing.", templateName);
            throw new IllegalArgumentException("Invalid template name", e);
        } catch (IOException e) {
            log.error("Failed to delete index template '{}' with error", templateName, e);
            throw e;
        }
        return null;
    }

    @MonitoredFunction(className = Operations.CLIENT_NAME, method = Operations.DELETE_INDEX_TEMPLATE)
    private AcknowledgedResponse deleteIndexTemplate(
            final E storage,
            final DeleteComposableIndexTemplateRequest request
    )
            throws IOException {
        return storage.client().indices().deleteIndexTemplate(request, RequestOptions.DEFAULT);
    }
}
