package com.phonepe.commons.columbus.elastic9.internal.storage.factory;

import com.phonepe.commons.columbus.elastic9.internal.storage.factory.initializer.DefaultElasticClientInitializer;
import com.phonepe.commons.columbus.elastic9.internal.storage.toolkit.DefaultElasticToolkitInitializer;
import com.phonepe.commons.columbus.elastic9.storage.configuration.ElasticConfiguration;
import com.phonepe.commons.columbus.elastic9.storage.factory.ElasticClientInitializer;
import com.phonepe.commons.columbus.elastic9.storage.factory.ElasticClientProvider;
import com.phonepe.commons.columbus.elastic9.storage.toolkit.ElasticToolkitInitializer;
import com.phonepe.commons.columbus.registry.factory.RegistryKey;
import com.phonepe.commons.columbus.storage.factory.impl.ColumbusStorageFactoryContext;

/**
 * An {@link ElasticClientProvider} that is used to create
 * {@link com.phonepe.commons.columbus.elastic9.internal.storage.ColumbusElasticClient ColumbusElasticClient}
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
