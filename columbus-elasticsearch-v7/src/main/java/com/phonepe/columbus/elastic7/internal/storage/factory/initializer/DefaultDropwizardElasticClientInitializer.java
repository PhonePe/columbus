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
package com.phonepe.columbus.elastic7.internal.storage.factory.initializer;

import com.phonepe.columbus.common.initializer.Initializer;
import com.phonepe.columbus.common.initializer.LateInit;
import com.phonepe.columbus.elastic7.internal.storage.lifecycle.ElasticClientHealthCheck;
import com.phonepe.columbus.elastic7.internal.storage.lifecycle.ElasticClientManaged;
import com.phonepe.columbus.elastic7.internal.storage.utils.ElasticRestClientUtils;
import com.phonepe.columbus.elastic7.storage.ElasticClient;
import com.phonepe.columbus.elastic7.storage.factory.DropwizardElasticClientInitializer;
import io.dropwizard.setup.Environment;

/**
 * This class includes Dropwizard features while creating the {@link ElasticClient}.
 */
public class DefaultDropwizardElasticClientInitializer extends DefaultElasticClientInitializer
        implements DropwizardElasticClientInitializer {

    private final LateInit<Environment> environment = new LateInit<>("environment");

    @Override
    public void use(final Environment environment) {
        this.environment.set(environment);
    }

    public Initializer<Environment> getEnvironment() {
        return environment;
    }

    @Override
    protected boolean canInitializeElasticClient() {
        return super.canInitializeElasticClient() && getEnvironment().isPresent();
    }

    @Override
    protected ElasticClient<?> initializeElasticClient() {
        final var client = super.initializeElasticClient();

        bindLifecycle(getEnvironment().get(), client);

        return client;
    }

    private static void bindLifecycle(final Environment environment, final ElasticClient<?> elasticClient) {
        environment.lifecycle().manage(new ElasticClientManaged(elasticClient.client()));
        final var healthCheckName = ElasticRestClientUtils.createHealthCheckName(elasticClient.name());
        final var healthCheck = ElasticClientHealthCheck.builder()
                .name(healthCheckName)
                .client(elasticClient)
                .failOnDegrade(elasticClient.getConfiguration().getHosts().isYellowUnhealthy())
                .build();
        environment.healthChecks().register(healthCheckName, healthCheck);
    }
}
