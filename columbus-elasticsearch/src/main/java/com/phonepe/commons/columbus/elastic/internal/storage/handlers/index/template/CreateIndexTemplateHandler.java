package com.phonepe.commons.columbus.elastic.internal.storage.handlers.index.template;

import com.phonepe.commons.columbus.elastic.common.Constants.Operations;
import com.phonepe.commons.columbus.elastic.storage.ElasticClient;
import com.phonepe.commons.columbus.elastic.storage.handlers.ElasticStorageCommandHandler;
import com.phonepe.commons.columbus.registry.factory.RegistryKey;
import com.phonepe.commons.columbus.storage.commands.index.template.CreateIndexTemplateCommand;
import io.appform.functionmetrics.MonitoredFunction;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.indices.PutComposableIndexTemplateRequest;
import org.elasticsearch.cluster.metadata.ComposableIndexTemplate;
import org.elasticsearch.cluster.metadata.Template;
import org.elasticsearch.common.compress.CompressedXContent;
import org.elasticsearch.common.settings.Settings;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;

/**
 * Create a new index template in the ElasticSearch cluster, with the given name, patterns and properties.
 *
 * @param <E> the type of the Elastic client.
 */
@Slf4j
@RegistryKey(name = CreateIndexTemplateCommand.COMMAND_NAME, type = CreateIndexTemplateCommand.class)
public class CreateIndexTemplateHandler<E extends ElasticClient<E>>
        implements ElasticStorageCommandHandler<Void, CreateIndexTemplateCommand, E> {

    @Override
    @SneakyThrows
    public Void handle(final CreateIndexTemplateCommand command, final E storage) {
        final var properties = command.getProperties();
        final var templateName = command.getTemplateName();
        final var settings = Objects.<Map<String, Object>>requireNonNullElseGet(properties.getSettings(), Map::of);
        final var mapping = Objects.requireNonNullElseGet(properties.getSchema(), Map::of);

        final var template = new Template(
                Settings.builder().loadFromMap(settings).build(),
                new CompressedXContent(storage.toolkit().serDe().toBytes(mapping)),
                Map.of());
        final var indexTemplate = new ComposableIndexTemplate(
                command.getIndexPatterns(), template, null, null, null, null);
        final var request = new PutComposableIndexTemplateRequest()
                .name(templateName)
                .indexTemplate(indexTemplate);

        try {
            final var ack = createIndexTemplate(storage, request).isAcknowledged();
            if (!ack) {
                log.warn("Creation of index template not acknowledged for template: '{}'", templateName);
            }
        } catch (IOException e) {
            log.error("Failed to create index template '{}' with error", templateName, e);
            throw e;
        }
        return null;
    }

    @MonitoredFunction(className = Operations.CLIENT_NAME, method = Operations.CREATE_INDEX_TEMPLATE)
    private AcknowledgedResponse createIndexTemplate(final E storage, final PutComposableIndexTemplateRequest request)
            throws IOException {
        return storage.client().indices().putIndexTemplate(request, RequestOptions.DEFAULT);
    }
}
