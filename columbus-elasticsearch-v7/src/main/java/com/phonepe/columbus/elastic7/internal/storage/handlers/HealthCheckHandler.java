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
package com.phonepe.columbus.elastic7.internal.storage.handlers;

import com.phonepe.columbus.elastic7.common.Constants.Operations;
import com.phonepe.columbus.elastic7.internal.storage.utils.EnumTransforms;
import com.phonepe.columbus.elastic7.storage.ElasticClient;
import com.phonepe.columbus.elastic7.storage.handlers.ElasticStorageCommandHandler;
import com.phonepe.columbus.registry.factory.RegistryKey;
import com.phonepe.columbus.storage.commands.HealthCheckCommand;
import com.phonepe.columbus.storage.commands.HealthCheckCommand.HealthStatus;
import io.appform.functionmetrics.MonitoredFunction;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.admin.cluster.health.ClusterHealthRequest;
import org.elasticsearch.action.admin.cluster.health.ClusterHealthResponse;
import org.elasticsearch.client.RequestOptions;

import java.io.IOException;

/**
 * Check the health of the ElasticSearch cluster. If the client fails to check the status, it is assumed to be
 * unhealthy.
 *
 * @param <E> the type of the Elastic client.
 */
@Slf4j
@RegistryKey(name = HealthCheckCommand.COMMAND_NAME, type = HealthCheckCommand.class)
public class HealthCheckHandler<E extends ElasticClient<E>>
        implements ElasticStorageCommandHandler<HealthStatus, HealthCheckCommand, E> {

    @Override
    public HealthStatus handle(final HealthCheckCommand command, final E storage) {
        try {
            return EnumTransforms.toHealthStatus(healthCheck(storage).getStatus());
        } catch (IOException e) {
            log.error("Failed to check cluster health", e);
            return HealthStatus.UNHEALTHY;
        }
    }

    @MonitoredFunction(className = Operations.CLIENT_NAME, method = Operations.HEALTH_CHECK)
    private ClusterHealthResponse healthCheck(final E storage) throws IOException {
        return storage.client().cluster().health(new ClusterHealthRequest(), RequestOptions.DEFAULT);
    }
}
