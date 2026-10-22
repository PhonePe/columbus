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
package com.phonepe.columbus.elastic7.internal.storage.handlers.entity.bulk;

import com.phonepe.columbus.dao.id.Id;
import com.phonepe.columbus.dao.misc.OperationFailure;
import com.phonepe.columbus.elastic7.common.Constants.Operations;
import com.phonepe.columbus.elastic7.internal.storage.utils.ElasticClientUtils;
import com.phonepe.columbus.elastic7.internal.storage.utils.EntityTransforms;
import com.phonepe.columbus.elastic7.internal.storage.utils.EnumTransforms;
import com.phonepe.columbus.elastic7.storage.ElasticClient;
import com.phonepe.columbus.elastic7.storage.handlers.ElasticStorageCommandHandler;
import com.phonepe.columbus.elastic7.storage.toolkit.ElasticToolkit;
import com.phonepe.columbus.registry.factory.RegistryKey;
import com.phonepe.columbus.storage.commands.entity.bulk.BulkDeleteCommand;
import com.phonepe.columbus.storage.commands.entity.bulk.BulkDeleteCommand.Result;
import com.phonepe.columbus.storage.models.entity.EntityId;
import io.appform.functionmetrics.MonitoredFunction;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.client.RequestOptions;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.phonepe.columbus.common.Predicates.applyIfNotNull;

/**
 * Delete a batch of values from an ElasticSearch index.
 *
 * @param <E> the type of the Elastic client.
 */
@Slf4j
@RegistryKey(name = BulkDeleteCommand.COMMAND_NAME, type = BulkDeleteCommand.class)
public class BulkDeleteHandler<E extends ElasticClient<E>>
        implements ElasticStorageCommandHandler<BulkDeleteCommand.Result, BulkDeleteCommand, E> {

    @Override
    @SneakyThrows
    public BulkDeleteCommand.Result handle(final BulkDeleteCommand command, final E storage) {
        final var context = new Context();
        context.toolkit = storage.toolkit();

        final var request = createRequest(command, context);

        final BulkResponse response;
        try {
            response = bulkDelete(storage, request);
        } catch (IOException e) {
            log.error("Failed to delete from index '{}' with {} ids", context.indexName, context.docCount, e);
            throw e;
        }

        return parseResult(response, context);
    }

    @MonitoredFunction(className = Operations.CLIENT_NAME, method = Operations.BULK_DELETE_DOC)
    private BulkResponse bulkDelete(final E storage, final BulkRequest request) throws IOException {
        return storage.client().bulk(request, RequestOptions.DEFAULT);
    }

    private BulkRequest createRequest(final BulkDeleteCommand command, final Context context) {
        context.docCount = command.getIds().size();
        context.idMap = new HashMap<>(context.docCount);
        context.indexName = ElasticClientUtils.resolveIndexName(context.toolkit, command.getIndex());

        final var request = new BulkRequest(context.indexName);
        command.getIds().stream()
                .map(id -> createDeleteRequest(id, context))
                .forEachOrdered(request::add);
        applyIfNotNull(command.getExecutionPolicy(), EnumTransforms::toRefreshPolicy, request::setRefreshPolicy);
        return request;
    }

    private Result parseResult(final BulkResponse response, final Context context) {
        final var builder = BulkDeleteCommand.Result.builder();
        final var idMap = context.idMap;

        response.forEach(item -> {
            final var id = idMap.get(item.getId());
            if (item.isFailed()) {
                final var failure = item.getFailure();
                builder.failure(id, OperationFailure.builder()
                        .message(failure.getMessage())
                        .exception(failure.getCause())
                        .build());
            } else if (item.getResponse().getResult() == DocWriteResponse.Result.NOT_FOUND) {
                builder.notFound(id);
            }
        });
        return builder.build();
    }

    private DeleteRequest createDeleteRequest(final EntityId entity, final Context context) {
        final var id = ElasticClientUtils.resolveId(context.toolkit, entity.getId());
        final var node = ElasticClientUtils.resolveNodeId(context.toolkit, entity.getNodeId());
        context.idMap.put(id, entity.getId());
        return new DeleteRequest()
                .id(id)
                .routing(EntityTransforms.getSingleRouting(node));
    }

    private static final class Context {

        private ElasticToolkit toolkit;
        private String indexName;
        private int docCount;
        private Map<String, Id> idMap;
    }
}
