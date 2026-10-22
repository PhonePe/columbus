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
package com.phonepe.columbus.elastic9.internal.storage.handlers.entity.status;

import co.elastic.clients.elasticsearch._types.ElasticsearchException;
import co.elastic.clients.elasticsearch.tasks.GetTasksRequest;
import co.elastic.clients.elasticsearch.tasks.GetTasksResponse;
import com.phonepe.columbus.elastic9.common.Constants.Operations;
import com.phonepe.columbus.elastic9.storage.ElasticClient;
import com.phonepe.columbus.elastic9.storage.handlers.ElasticStorageCommandHandler;
import com.phonepe.columbus.registry.factory.RegistryKey;
import com.phonepe.columbus.storage.commands.entity.status.GetCopyStatusCommand;
import com.phonepe.columbus.storage.commands.entity.status.GetCopyStatusCommand.Result;
import io.appform.functionmetrics.MonitoredFunction;
import jakarta.json.JsonObject;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Slf4j
@RegistryKey(name = GetCopyStatusCommand.COMMAND_NAME, type = GetCopyStatusCommand.class)
public class GetCopyStatusHandler<E extends ElasticClient<E>>
        implements ElasticStorageCommandHandler<Optional<Result>, GetCopyStatusCommand, E> {

    @Override
    @SneakyThrows
    public Optional<Result> handle(final GetCopyStatusCommand command, final E storage) {
        final var request = buildRequest(command);

        final GetTasksResponse response;
        try {
            response = status(storage, request);
        } catch (final IOException e) {
            log.error("Failed to get status of reindex: {} with error", request, e);
            throw e;
        }

        return Optional.of(parseResult(response));
    }

    @MonitoredFunction(className = Operations.CLIENT_NAME, method = Operations.REINDEX_STATUS)
    private GetTasksResponse status(final E storage, final GetTasksRequest request) throws IOException,
            ElasticsearchException {
        return storage.client().tasks().get(request);
    }

    private GetTasksRequest buildRequest(final GetCopyStatusCommand command) {
        return new GetTasksRequest.Builder().taskId(command.getTaskId()).build();
    }

    private Result parseResult(final GetTasksResponse response) {
        final var taskInfo = response.task();
        final var builder = Result.builder()
                .completed(response.completed())
                .startTime(taskInfo.startTimeInMillis())
                .elapsedTime(TimeUnit.NANOSECONDS.toMillis(taskInfo.runningTimeInNanos()));

        final JsonObject status = taskInfo.status().toJson().asJsonObject();
        builder.total(status.getInt("total"))
                    .created(status.getInt("created"))
                    .updated(status.getInt("updated"));

        return builder.build();
    }
}
