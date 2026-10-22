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
package com.phonepe.columbus.elastic7.internal.storage.lifecycle;

import com.codahale.metrics.health.HealthCheck;
import com.phonepe.columbus.elastic7.storage.ElasticClient;
import com.phonepe.columbus.storage.commands.HealthCheckCommand;
import com.phonepe.columbus.storage.commands.HealthCheckCommand.HealthStatus;
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
