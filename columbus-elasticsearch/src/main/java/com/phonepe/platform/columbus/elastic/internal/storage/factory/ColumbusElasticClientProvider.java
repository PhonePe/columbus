package com.phonepe.platform.columbus.elastic.internal.storage.factory;

import com.phonepe.platform.columbus.dropwizard.storage.factory.DropwizardStorageFactoryContext;
import com.phonepe.platform.columbus.elastic.internal.storage.factory.initializer.DefaultElasticClientInitializer;
import com.phonepe.platform.columbus.elastic.internal.storage.toolkit.DefaultElasticToolkitInitializer;
import com.phonepe.platform.columbus.elastic.storage.configuration.ElasticConfiguration;
import com.phonepe.platform.columbus.elastic.storage.factory.ElasticClientInitializer;
import com.phonepe.platform.columbus.elastic.storage.factory.ElasticClientProvider;
import com.phonepe.platform.columbus.elastic.storage.toolkit.ElasticToolkitInitializer;
import com.phonepe.platform.columbus.registry.factory.RegistryKey;

/**
 * An {@link ElasticClientProvider} that is used to create
 * {@link com.phonepe.platform.columbus.elastic.internal.storage.ColumbusElasticClient ColumbusElasticClient}
 * instances with the help of {@link DefaultElasticClientInitializer}.
 *
 * @param <C> the type of the context required.
 */
@RegistryKey(name = ElasticConfiguration.TYPE, type = ElasticConfiguration.class)
public final class ColumbusElasticClientProvider<C extends DropwizardStorageFactoryContext<C>>
        extends ElasticClientProvider<C> {

    @Override
    protected ElasticClientInitializer getInitializer() {
        return new DefaultElasticClientInitializer();
    }

    @Override
    protected ElasticToolkitInitializer getElasticToolkitInitializer() {
        return new DefaultElasticToolkitInitializer();
    }
}
