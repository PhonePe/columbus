package com.phonepe.platform.columbus.elastic.internal.storage.lifecycle;

import com.codahale.metrics.health.HealthCheck;
import com.phonepe.platform.columbus.elastic.storage.ElasticClient;
import com.phonepe.platform.columbus.storage.commands.HealthCheckCommand;
import com.phonepe.platform.columbus.storage.commands.HealthCheckCommand.HealthStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

/**
 * An object that monitors the health of an {@link ElasticClient}.
 */
@Slf4j
@Builder
public class ElasticClientHealthCheck extends HealthCheck {

    private static final HealthCheckCommand COMMAND = HealthCheckCommand.builder().build();

    @Getter
    @NonNull
    private final String name;

    @NonNull
    private final ElasticClient<?> client;

    private final boolean failOnDegrade;

    @Override
    protected Result check() {
        final var status = COMMAND.execute(client);
        log.info("[HEALTH] {}: {}", name, status);
        final var message = "Status: " + status;

        return HealthStatus.UNHEALTHY == status || (failOnDegrade && HealthStatus.DEGRADED == status)
                ? Result.unhealthy(message)
                : Result.healthy(message);
    }
}
