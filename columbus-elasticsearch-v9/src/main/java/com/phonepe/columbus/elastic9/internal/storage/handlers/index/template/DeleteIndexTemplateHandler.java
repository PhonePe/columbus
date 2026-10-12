package com.phonepe.columbus.elastic9.internal.storage.handlers.index.template;

import co.elastic.clients.elasticsearch._types.AcknowledgedResponse;
import co.elastic.clients.elasticsearch._types.ElasticsearchException;
import co.elastic.clients.elasticsearch.indices.DeleteIndexTemplateRequest;
import com.phonepe.columbus.elastic9.common.Constants.Operations;
import com.phonepe.columbus.elastic9.storage.ElasticClient;
import com.phonepe.columbus.elastic9.storage.handlers.ElasticStorageCommandHandler;
import com.phonepe.columbus.registry.factory.RegistryKey;
import com.phonepe.columbus.storage.commands.index.template.DeleteIndexTemplateCommand;
import io.appform.functionmetrics.MonitoredFunction;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

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

        final var request = new DeleteIndexTemplateRequest.Builder()
                .name(templateName)
                .build();

        try {
            final var ack = deleteIndexTemplate(storage, request).acknowledged();
            if (!ack) {
                log.warn("Deletion of index template not acknowledged for template: '{}'", templateName);
            }
        } catch (final ElasticsearchException e) {
            log.error("Failed to perform deletion, likely the template '{}' is missing.", templateName);
            throw new IllegalArgumentException("Invalid template name", e);
        } catch (final IOException e) {
            log.error("Failed to delete index template '{}' with error", templateName, e);
            throw e;
        }
        return null;
    }

    @MonitoredFunction(className = Operations.CLIENT_NAME, method = Operations.DELETE_INDEX_TEMPLATE)
    private AcknowledgedResponse deleteIndexTemplate(
            final E storage,
            final DeleteIndexTemplateRequest request)
            throws IOException {
        return storage.client().indices().deleteIndexTemplate(request);
    }
}
