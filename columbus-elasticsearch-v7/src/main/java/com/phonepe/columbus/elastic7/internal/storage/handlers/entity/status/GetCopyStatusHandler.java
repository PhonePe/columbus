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
package com.phonepe.columbus.elastic7.internal.storage.handlers.entity.status;

import com.phonepe.columbus.elastic7.common.Constants.Operations;
import com.phonepe.columbus.elastic7.storage.ElasticClient;
import com.phonepe.columbus.elastic7.storage.handlers.ElasticStorageCommandHandler;
import com.phonepe.columbus.registry.factory.RegistryKey;
import com.phonepe.columbus.storage.commands.entity.status.GetCopyStatusCommand;
import com.phonepe.columbus.storage.commands.entity.status.GetCopyStatusCommand.Result;
import io.appform.functionmetrics.MonitoredFunction;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.tasks.GetTaskRequest;
import org.elasticsearch.client.tasks.GetTaskResponse;
import org.elasticsearch.index.reindex.BulkByScrollTask;
import org.elasticsearch.tasks.RawTaskStatus;
import org.elasticsearch.tasks.TaskId;

import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Slf4j
@RegistryKey(name = GetCopyStatusCommand.COMMAND_NAME, type = GetCopyStatusCommand.class)
public class GetCopyStatusHandler<E extends ElasticClient<E>>
        implements ElasticStorageCommandHandler<Optional<GetCopyStatusCommand.Result>, GetCopyStatusCommand, E> {

    @Override
    @SneakyThrows
    public Optional<GetCopyStatusCommand.Result> handle(final GetCopyStatusCommand command, final E storage) {
        final var request = buildRequest(command);

        final Optional<GetTaskResponse> response;
        try {
            response = status(storage, request);
        } catch (IOException e) {
            log.error("Failed to get status of reindex: {} with error", request, e);
            throw e;
        }

        return response.map(this::parseResult);
    }

    @MonitoredFunction(className = Operations.CLIENT_NAME, method = Operations.REINDEX_STATUS)
    private Optional<GetTaskResponse> status(final E storage, final GetTaskRequest request) throws IOException {
        return storage.client().tasks().get(request, RequestOptions.DEFAULT);
    }

    private GetTaskRequest buildRequest(final GetCopyStatusCommand command) {
        final var taskId = new TaskId(command.getTaskId());
        if (!taskId.isSet()) {
            throw new IllegalArgumentException("Invalid task id: '%s'".formatted(command.getTaskId()));
        }
        return new GetTaskRequest(taskId.getNodeId(), taskId.getId());
    }

    private GetCopyStatusCommand.Result parseResult(final GetTaskResponse response) {
        final var taskInfo = response.getTaskInfo();
        final var builder = Result.builder()
                .completed(response.isCompleted())
                .startTime(taskInfo.getStartTime())
                .elapsedTime(TimeUnit.NANOSECONDS.toMillis(taskInfo.getRunningTimeNanos()));

        if (taskInfo.getStatus() instanceof BulkByScrollTask.Status status) {
            builder.total(status.getTotal())
                    .created(status.getCreated())
                    .updated(status.getUpdated());
        } else if (taskInfo.getStatus() instanceof RawTaskStatus rawStatus) {
            final var status = rawStatus.toMap();
            builder.total(((Number) status.get("total")).longValue())
                    .created(((Number) status.get("created")).longValue())
                    .updated(((Number) status.get("updated")).longValue());
        }

        return builder.build();
    }
}
