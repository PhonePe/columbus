package com.phonepe.commons.columbus.elastic9.internal.storage.factory;

import com.phonepe.commons.columbus.dropwizard.storage.factory.DropwizardStorageFactoryContext;
import com.phonepe.commons.columbus.elastic9.internal.storage.factory.initializer.DefaultDropwizardElasticClientInitializer;
import com.phonepe.commons.columbus.elastic9.internal.storage.toolkit.DefaultElasticToolkitInitializer;
import com.phonepe.commons.columbus.elastic9.storage.configuration.ElasticConfiguration;
import com.phonepe.commons.columbus.elastic9.storage.factory.DropwizardElasticClientInitializer;
import com.phonepe.commons.columbus.elastic9.storage.factory.ElasticClientProvider;
import com.phonepe.commons.columbus.elastic9.storage.toolkit.ElasticToolkitInitializer;
import com.phonepe.commons.columbus.registry.factory.RegistryKey;

/**
 * An {@link ElasticClientProvider} that is used to create
 * {@link com.phonepe.commons.columbus.elastic9.internal.storage.ColumbusElasticClient ColumbusElasticClient}
 * instances with the help of {@link DefaultDropwizardElasticClientInitializer}.
 *
 * @param <C> the type of the context required.
 */
@RegistryKey(name = ElasticConfiguration.TYPE, type = ElasticConfiguration.class, priority = 1)
public final class ColumbusDropwizardElasticClientProvider<C extends DropwizardStorageFactoryContext<C>>
        extends ElasticClientProvider<C> {

    @Override
    protected DropwizardElasticClientInitializer getInitializer(
            final ElasticConfiguration configuration,
            final C context
    ) {
        final var initializer = new DefaultDropwizardElasticClientInitializer();
        initializer.use(context.dropwizardEnvironment());
        return initializer;
    }

    @Override
    protected ElasticToolkitInitializer getElasticToolkitInitializer() {
        return new DefaultElasticToolkitInitializer();
    }
}
