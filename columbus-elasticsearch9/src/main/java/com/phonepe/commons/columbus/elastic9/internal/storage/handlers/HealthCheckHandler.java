package com.phonepe.commons.columbus.elastic9.internal.storage.handlers;

import co.elastic.clients.elasticsearch.cluster.HealthRequest;
import co.elastic.clients.elasticsearch.cluster.HealthResponse;
import com.phonepe.commons.columbus.elastic9.common.Constants.Operations;
import com.phonepe.commons.columbus.elastic9.internal.storage.utils.EnumTransforms;
import com.phonepe.commons.columbus.elastic9.storage.ElasticClient;
import com.phonepe.commons.columbus.elastic9.storage.handlers.ElasticStorageCommandHandler;
import com.phonepe.commons.columbus.registry.factory.RegistryKey;
import com.phonepe.commons.columbus.storage.commands.HealthCheckCommand;
import com.phonepe.commons.columbus.storage.commands.HealthCheckCommand.HealthStatus;
import io.appform.functionmetrics.MonitoredFunction;
import lombok.extern.slf4j.Slf4j;

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
            return EnumTransforms.toHealthStatus(healthCheck(storage).status());
        } catch (final IOException e) {
            log.error("Failed to check cluster health", e);
            return HealthStatus.UNHEALTHY;
        }
    }

    @MonitoredFunction(className = Operations.CLIENT_NAME, method = Operations.HEALTH_CHECK)
    private HealthResponse healthCheck(final E storage) throws IOException {
        return storage.client().cluster().health(new HealthRequest.Builder().build());
    }
}
