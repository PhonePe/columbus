package com.phonepe.commons.columbus.elastic.internal.storage.handlers.entity.bulk;

import com.phonepe.commons.columbus.dao.id.Id;
import com.phonepe.commons.columbus.dao.misc.OperationFailure;
import com.phonepe.commons.columbus.elastic.common.Constants.Operations;
import com.phonepe.commons.columbus.elastic.internal.search.utils.MiscTransforms;
import com.phonepe.commons.columbus.elastic.internal.storage.utils.ElasticClientUtils;
import com.phonepe.commons.columbus.elastic.internal.storage.utils.EntityTransforms;
import com.phonepe.commons.columbus.elastic.storage.ElasticClient;
import com.phonepe.commons.columbus.elastic.storage.handlers.ElasticStorageCommandHandler;
import com.phonepe.commons.columbus.elastic.storage.toolkit.ElasticToolkit;
import com.phonepe.commons.columbus.registry.factory.RegistryKey;
import com.phonepe.commons.columbus.storage.commands.entity.bulk.BulkGetCommand;
import com.phonepe.commons.columbus.storage.commands.entity.bulk.BulkGetCommand.Result;
import com.phonepe.commons.columbus.storage.models.entity.EntityId;
import io.appform.functionmetrics.MonitoredFunction;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.get.MultiGetRequest;
import org.elasticsearch.action.get.MultiGetRequest.Item;
import org.elasticsearch.action.get.MultiGetResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.search.fetch.subphase.FetchSourceContext;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static com.phonepe.commons.columbus.common.Predicates.applyIfNotNull;

/**
 * Get a batch of values from an ElasticSearch index by id.
 *
 * @param <E> the type of the Elastic client.
 */
@Slf4j
@RegistryKey(name = BulkGetCommand.COMMAND_NAME, type = BulkGetCommand.class)
public class BulkGetHandler<T, E extends ElasticClient<E>>
        implements ElasticStorageCommandHandler<BulkGetCommand.Result<T>, BulkGetCommand<T>, E> {

    @Override
    @SneakyThrows
    @SuppressWarnings("java:S1854") // useless variable? Fix sonar
    public BulkGetCommand.Result<T> handle(final BulkGetCommand<T> command, final E storage) {
        final var context = new Context();
        context.toolkit = storage.toolkit();

        final var request = createRequest(command, context);

        final MultiGetResponse response;
        try {
            response = bulkGet(storage, request);
        } catch (IOException e) {
            log.error("Failed to get from index '{}' with {} ids", context.indexName, context.docCount, e);
            throw e;
        }

        return parseResult(response, command, context);
    }

    @MonitoredFunction(className = Operations.CLIENT_NAME, method = Operations.BULK_GET_DOC)
    private MultiGetResponse bulkGet(final E storage, final MultiGetRequest request) throws IOException {
        return storage.client().mget(request, RequestOptions.DEFAULT);
    }

    private MultiGetRequest createRequest(final BulkGetCommand<T> command, final Context context) {
        context.indexName = ElasticClientUtils.resolveIndexName(context.toolkit, command.getIndex());
        context.docCount = command.getIds().size();
        context.idMap = new HashMap<>(context.docCount);
        context.preferences = new HashSet<>();
        context.fetchSourceContext = Optional.ofNullable(command.getProjection())
                .map(MiscTransforms::toElastic)
                .orElse(null);

        final var request = new MultiGetRequest();
        command.getIds().stream()
                .map(entityId -> createItem(entityId, context))
                .forEachOrdered(request::add);
        request.preference(getPreference(context.preferences));
        return request;
    }

    private Result<T> parseResult(
            final MultiGetResponse response,
            final BulkGetCommand<T> command,
            final Context context
    ) {
        final var builder = BulkGetCommand.Result.<T>builder();
        final var idMap = context.idMap;
        final var serDe = context.toolkit.serDe();

        response.forEach(item -> {
            final var id = idMap.get(item.getId());
            if (item.isFailed()) {
                final var failure = item.getFailure();
                builder.failure(id, OperationFailure.builder()
                        .message(failure.getMessage())
                        .exception(failure.getFailure())
                        .build());
            } else if (item.getResponse().isExists()) {
                builder.entity(id, serDe.fromBytes(item.getResponse().getSourceAsBytes(), command.getEntityType()));
            } else {
                builder.notFound(id);
            }
        });

        return builder.build();
    }

    private static Item createItem(final EntityId entityId, final Context context) {
        final var id = ElasticClientUtils.resolveId(context.toolkit, entityId.getId());
        final var node = ElasticClientUtils.resolveNodeId(context.toolkit, entityId.getNodeId());
        context.idMap.put(id, entityId.getId());
        applyIfNotNull(node.getPreference(), context.preferences::add);
        return new Item(context.indexName, id)
                .routing(EntityTransforms.getSingleRouting(node))
                .fetchSourceContext(context.fetchSourceContext);
    }

    private static String getPreference(final Set<String> preferences) {
        final var preference = preferences.stream().findFirst().orElse(null);
        if (preference == null) {
            return null;
        }
        if (preferences.size() > 1) {
            log.warn("Bulk get only supports one replica id, using '{}' of {}", preference, preferences.size());
        }
        return preference;
    }

    private static final class Context {

        private ElasticToolkit toolkit;
        private String indexName;
        private int docCount;
        private Map<String, Id> idMap;
        private Set<String> preferences;
        private FetchSourceContext fetchSourceContext;
    }
}
