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
package com.phonepe.columbus.elastic7.internal.storage.handlers.index;

import com.phonepe.columbus.elastic7.common.Constants.Operations;
import com.phonepe.columbus.elastic7.storage.ElasticClient;
import com.phonepe.columbus.elastic7.storage.handlers.ElasticStorageCommandHandler;
import com.phonepe.columbus.registry.factory.RegistryKey;
import com.phonepe.columbus.storage.commands.index.DeleteIndexCommand;
import io.appform.functionmetrics.MonitoredFunction;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.RequestOptions;

import java.io.IOException;

/**
 * Delete an existing index.
 * <p>
 * The result indicates if the server has acknowledged the request. The actual deletion might occur later, resulting in
 * some (short-lived) inconsistency.
 *
 * @param <E> the type of the Elastic client.
 */
@Slf4j
@RegistryKey(name = DeleteIndexCommand.COMMAND_NAME, type = DeleteIndexCommand.class)
public class DeleteIndexHandler<E extends ElasticClient<E>>
        implements ElasticStorageCommandHandler<Boolean, DeleteIndexCommand, E> {

    @Override
    @SneakyThrows
    public Boolean handle(final DeleteIndexCommand command, final E storage) {
        final var indexName = storage.toolkit().indexResolver().resolve(command.getIndex()).getName();
        final var request = new DeleteIndexRequest(indexName);

        try {
            final var ack = deleteIndex(storage, request).isAcknowledged();
            if (!ack) {
                log.warn("Deletion not acknowledged for index: '{}'", indexName);
            }
            return ack;
        } catch (IOException e) {
            log.error("Failed to delete index '{}' with error", indexName, e);
            throw e;
        }
    }

    @MonitoredFunction(className = Operations.CLIENT_NAME, method = Operations.DELETE_INDEX)
    private AcknowledgedResponse deleteIndex(final E storage, final DeleteIndexRequest request) throws IOException {
        return storage.client().indices().delete(request, RequestOptions.DEFAULT);
    }
}
