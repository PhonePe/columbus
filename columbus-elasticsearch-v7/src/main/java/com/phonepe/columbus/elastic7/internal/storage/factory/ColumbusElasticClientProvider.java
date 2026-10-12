package com.phonepe.columbus.elastic7.internal.storage.factory;

import com.phonepe.columbus.elastic7.internal.storage.factory.initializer.DefaultElasticClientInitializer;
import com.phonepe.columbus.elastic7.internal.storage.toolkit.DefaultElasticToolkitInitializer;
import com.phonepe.columbus.elastic7.storage.configuration.ElasticConfiguration;
import com.phonepe.columbus.elastic7.storage.factory.ElasticClientInitializer;
import com.phonepe.columbus.elastic7.storage.factory.ElasticClientProvider;
import com.phonepe.columbus.elastic7.storage.toolkit.ElasticToolkitInitializer;
import com.phonepe.columbus.registry.factory.RegistryKey;
import com.phonepe.columbus.storage.factory.impl.ColumbusStorageFactoryContext;

/**
 * An {@link ElasticClientProvider} that is used to create
 * {@link com.phonepe.columbus.elastic7.internal.storage.ColumbusElasticClient ColumbusElasticClient}
 * instances with the help of {@link DefaultElasticClientInitializer}.
 *
 * @param <C> the type of the context required.
 */
@RegistryKey(name = ElasticConfiguration.TYPE, type = ElasticConfiguration.class)
public final class ColumbusElasticClientProvider<C extends ColumbusStorageFactoryContext<C>>
        extends ElasticClientProvider<C> {

    @Override
    protected ElasticClientInitializer getInitializer(
            final ElasticConfiguration configuration,
            final C context
    ) {
        return new DefaultElasticClientInitializer();
    }

    @Override
    protected ElasticToolkitInitializer getElasticToolkitInitializer() {
        return new DefaultElasticToolkitInitializer();
    }
}
