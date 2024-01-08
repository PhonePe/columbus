package com.phonepe.commons.columbus.elastic.internal.storage.factory.initializer;

import com.phonepe.commons.columbus.command.CommandHelpers;
import com.phonepe.commons.columbus.core.utils.RegistryCreators;
import com.phonepe.commons.columbus.elastic.internal.storage.ColumbusElasticClient;
import com.phonepe.commons.columbus.elastic.internal.storage.lifecycle.ElasticClientHealthCheck;
import com.phonepe.commons.columbus.elastic.internal.storage.lifecycle.ElasticClientManaged;
import com.phonepe.commons.columbus.elastic.internal.storage.utils.ElasticRestClientUtils;
import com.phonepe.commons.columbus.elastic.storage.ElasticClient;
import com.phonepe.commons.columbus.elastic.storage.toolkit.ElasticToolkit;
import com.phonepe.commons.columbus.reflect.Generics;
import com.phonepe.commons.columbus.storage.StorageCommandHandler;
import io.dropwizard.setup.Environment;
import org.elasticsearch.client.RestHighLevelClient;

/**
 * This class defines the default behaviour for creating the {@link ElasticClient}.
 */
public class DefaultElasticClientInitializer extends LazyElasticClientInitializer {

    @Override
    protected boolean canInitializeRestClient() {
        return getConfiguration().isPresent();
    }

    @Override
    protected RestHighLevelClient initializeRestClient() {
        return ElasticRestClientUtils.createClient(getConfiguration().get());
    }

    @Override
    protected boolean canInitializeElasticToolkit() {
        return getSettings().isPresent()
                && getToolkit().isPresent()
                && getConfiguration().isPresent()
                && getElasticToolkitInitializer().isPresent();
    }

    @Override
    protected ElasticToolkit initializeElasticToolkit() {
        final var initializer = getElasticToolkitInitializer().get();
        initializer.use(getToolkit().get());
        initializer.use(getSettings().get());
        initializer.use(getConfiguration().get());
        return initializer.get();
    }

    @Override
    protected boolean canInitializeElasticClient() {
        return getSettings().isPresent()
                && getToolkit().isPresent()
                && getConfiguration().isPresent()
                && getRestClient().isPresent()
                && getEnvironment().isPresent()
                && getElasticToolkit().isPresent();
    }

    @Override
    protected ElasticClient<?> initializeElasticClient() {
        final var client = ColumbusElasticClient.builder()
                .resolver(CommandHelpers.resolver(RegistryCreators.createHandlerRegistry(
                        getToolkit().get().handlerFactory(),
                        Generics.typed(StorageCommandHandler.class),
                        ColumbusElasticClient.class,
                        getSettings().get().getCommandHandlerOverrides())))
                .configuration(getConfiguration().get())
                .client(getRestClient().get())
                .toolkit(getElasticToolkit().get())
                .build();

        bindLifecycle(getEnvironment().get(), client);

        return client;
    }

    protected void bindLifecycle(final Environment environment, final ElasticClient<?> elasticClient) {
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
