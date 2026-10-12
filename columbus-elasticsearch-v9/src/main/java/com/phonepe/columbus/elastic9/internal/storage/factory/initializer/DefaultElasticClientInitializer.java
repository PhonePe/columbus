package com.phonepe.columbus.elastic9.internal.storage.factory.initializer;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import com.phonepe.columbus.command.CommandHelpers;
import com.phonepe.columbus.core.utils.RegistryCreators;
import com.phonepe.columbus.elastic9.internal.storage.ColumbusElasticClient;
import com.phonepe.columbus.elastic9.internal.storage.utils.ElasticRestClientUtils;
import com.phonepe.columbus.elastic9.storage.ElasticClient;
import com.phonepe.columbus.elastic9.storage.toolkit.ElasticToolkit;
import com.phonepe.columbus.internal.serde.JsonSerDe;
import com.phonepe.columbus.reflect.Generics;
import com.phonepe.columbus.storage.StorageCommandHandler;

/**
 * This class defines the default behaviour for creating the {@link ElasticClient}.
 */
public class DefaultElasticClientInitializer extends LazyElasticClientInitializer {

    @Override
    protected boolean canInitializeRestClient() {
        return getConfiguration().isPresent() && getToolkit().isPresent();
    }

    @Override
    protected ElasticsearchClient initializeRestClient() {
        final JsonSerDe serDe = (JsonSerDe) getToolkit().get().serDe();
        return ElasticRestClientUtils.createClient(getConfiguration().get(), serDe.getMapper());
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
                && getElasticToolkit().isPresent();
    }

    @Override
    protected ElasticClient<?> initializeElasticClient() {
        return ColumbusElasticClient.builder()
                .resolver(CommandHelpers.resolver(RegistryCreators.createHandlerRegistry(
                        getToolkit().get().handlerFactory(),
                        Generics.typed(StorageCommandHandler.class),
                        ColumbusElasticClient.class,
                        getSettings().get().getCommandHandlerOverrides())))
                .configuration(getConfiguration().get())
                .client(getRestClient().get())
                .toolkit(getElasticToolkit().get())
                .build();
    }
}
