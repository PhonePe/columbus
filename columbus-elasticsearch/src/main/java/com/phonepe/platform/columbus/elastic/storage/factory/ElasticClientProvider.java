package com.phonepe.platform.columbus.elastic.storage.factory;

import com.phonepe.platform.columbus.dropwizard.storage.factory.DropwizardStorageFactoryContext;
import com.phonepe.platform.columbus.elastic.storage.configuration.ElasticConfiguration;
import com.phonepe.platform.columbus.storage.StorageClient;
import com.phonepe.platform.columbus.storage.factory.visitor.StorageClientProvider;

/**
 * A {@link StorageClientProvider} that creates ElasticSearch clients.
 *
 * @param <C> the type of the context required.
 */
public abstract class ElasticClientProvider<C extends DropwizardStorageFactoryContext<C>>
        implements StorageClientProvider<ElasticConfiguration, C> {

    @Override
    public StorageClient visit(final ElasticConfiguration configuration, final C context) {
        final var initializer = getInitializer();
        initializer.use(context.toolkit());
        initializer.use(context.settings());
        initializer.use(context.dropwizardEnvironment());
        initializer.use(configuration);
        return initializer.get();
    }

    protected abstract ElasticClientInitializer getInitializer();
}
