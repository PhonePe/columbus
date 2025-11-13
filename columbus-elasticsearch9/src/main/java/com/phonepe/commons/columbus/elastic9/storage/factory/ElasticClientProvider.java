package com.phonepe.commons.columbus.elastic9.storage.factory;

import com.phonepe.commons.columbus.elastic9.storage.configuration.ElasticConfiguration;
import com.phonepe.commons.columbus.elastic9.storage.toolkit.ElasticToolkitInitializer;
import com.phonepe.commons.columbus.storage.StorageClient;
import com.phonepe.commons.columbus.storage.factory.impl.ColumbusStorageFactoryContext;
import com.phonepe.commons.columbus.storage.factory.visitor.StorageClientProvider;

/**
 * A {@link StorageClientProvider} that creates ElasticSearch clients.
 *
 * @param <C> the type of the context required.
 */
public abstract class ElasticClientProvider<C extends ColumbusStorageFactoryContext<C>>
        implements StorageClientProvider<ElasticConfiguration, C> {

    @Override
    public StorageClient visit(final ElasticConfiguration configuration, final Void props, final C context) {
        final var initializer = getInitializer(configuration, context);
        initializer.use(context.toolkit());
        initializer.use(context.settings());
        initializer.use(configuration);
        initializer.use(getElasticToolkitInitializer());
        return initializer.get();
    }

    protected abstract ElasticClientInitializer getInitializer(
            final ElasticConfiguration configuration,
            final C context
    );

    protected abstract ElasticToolkitInitializer getElasticToolkitInitializer();
}
