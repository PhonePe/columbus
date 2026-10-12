package com.phonepe.columbus.elastic9.internal.storage.handlers.entity.bulk;

import co.elastic.clients.elasticsearch.core.MgetRequest;
import co.elastic.clients.elasticsearch.core.MgetResponse;
import co.elastic.clients.elasticsearch.core.mget.MultiGetOperation;
import co.elastic.clients.elasticsearch.core.search.SourceConfigParam;
import com.phonepe.columbus.dao.id.Id;
import com.phonepe.columbus.dao.misc.OperationFailure;
import com.phonepe.columbus.elastic9.common.Constants.Operations;
import com.phonepe.columbus.elastic9.internal.search.utils.MiscTransforms;
import com.phonepe.columbus.elastic9.internal.storage.utils.ElasticClientUtils;
import com.phonepe.columbus.elastic9.internal.storage.utils.EntityTransforms;
import com.phonepe.columbus.elastic9.storage.ElasticClient;
import com.phonepe.columbus.elastic9.storage.handlers.ElasticStorageCommandHandler;
import com.phonepe.columbus.elastic9.storage.toolkit.ElasticToolkit;
import com.phonepe.columbus.registry.factory.RegistryKey;
import com.phonepe.columbus.storage.commands.entity.bulk.BulkGetCommand;
import com.phonepe.columbus.storage.commands.entity.bulk.BulkGetCommand.Result;
import com.phonepe.columbus.storage.models.entity.EntityId;
import io.appform.functionmetrics.MonitoredFunction;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static com.phonepe.columbus.common.Predicates.applyIfNotNull;

/**
 * Get a batch of values from an ElasticSearch index by id.
 *
 * @param <E> the type of the Elastic client.
 */
@Slf4j
@RegistryKey(name = BulkGetCommand.COMMAND_NAME, type = BulkGetCommand.class)
public class BulkGetHandler<T, E extends ElasticClient<E>>
        implements ElasticStorageCommandHandler<Result<T>, BulkGetCommand<T>, E> {

    @Override
    @SneakyThrows
    public Result<T> handle(final BulkGetCommand<T> command, final E storage) {
        final var typeResolver = storage.toolkit().typeResolver();
        final var type = typeResolver.toRawType(command.getEntityType());
        final var context = new Context();
        context.toolkit = storage.toolkit();

        final var request = createRequest(command, context);

        final MgetResponse<T> response;
        try {
            response = bulkGet(storage, request, type);
        } catch (final IOException e) {
            log.error("Failed to get from index '{}' with {} ids", context.indexName, context.docCount, e);
            throw e;
        }

        return parseResult(response, context);
    }

    @MonitoredFunction(className = Operations.CLIENT_NAME, method = Operations.BULK_GET_DOC)
    private MgetResponse<T> bulkGet(final E storage, final MgetRequest request,
                                         final Type type) throws IOException {
        return storage.client().mget(request, type);
    }

    private MgetRequest createRequest(final BulkGetCommand<T> command, final Context context) {
        context.indexName = ElasticClientUtils.resolveIndexName(context.toolkit, command.getIndex());
        context.docCount = command.getIds().size();
        context.idMap = new HashMap<>(context.docCount);
        context.preferences = new HashSet<>();

        final var request = new MgetRequest.Builder();
        command.getIds().stream()
                .map(entityId -> createOperation(entityId, context))
                .forEachOrdered(request::docs);
        request.preference(getPreference(context.preferences));
        applyIfNotNull(command.getProjection(), projection -> {
            if (projection.disabled()) {
                request.source(new SourceConfigParam.Builder().fetch(false).build());
            }
            applyIfNotNull(projection.getIncludeFields(), request::sourceIncludes);
            applyIfNotNull(projection.getExcludeFields(), request::sourceExcludes);
        });
        return request.build();
    }

    private Result<T> parseResult(
            final MgetResponse<T> response,
            final Context context
    ) {
        final var builder = Result.<T>builder();
        final var idMap = context.idMap;

        response.docs().forEach(item -> {
            if (item.isFailure()) {
                final var failure = item.failure();
                final var error = failure.error();
                final var id = idMap.get(failure.id());
                builder.failure(id, OperationFailure.builder()
                        .message(MiscTransforms.fromElastic(error))
                        .build());
            } else if (item.isResult()) {
                final var result = item.result();
                final var id = idMap.get(result.id());

                if (item.result().found()) {
                    builder.entity(id, result.source());
                } else {
                    builder.notFound(id);
                }
            }
        });

        return builder.build();
    }

    private static MultiGetOperation createOperation(final EntityId entityId, final Context context) {
        final var id = ElasticClientUtils.resolveId(context.toolkit, entityId.getId());
        final var node = ElasticClientUtils.resolveNodeId(context.toolkit, entityId.getNodeId());
        context.idMap.put(id, entityId.getId());
        applyIfNotNull(node.getPreference(), context.preferences::add);
        return new MultiGetOperation.Builder()
                .index(context.indexName)
                .id(id)
                .routing(EntityTransforms.getSingleRouting(node))
                .build();
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
    }
}
