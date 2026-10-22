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
package com.phonepe.columbus.elastic7.internal.storage.factory;

import com.phonepe.columbus.dropwizard.storage.factory.DropwizardStorageFactoryContext;
import com.phonepe.columbus.elastic7.internal.storage.factory.initializer.DefaultDropwizardElasticClientInitializer;
import com.phonepe.columbus.elastic7.internal.storage.toolkit.DefaultElasticToolkitInitializer;
import com.phonepe.columbus.elastic7.storage.configuration.ElasticConfiguration;
import com.phonepe.columbus.elastic7.storage.factory.DropwizardElasticClientInitializer;
import com.phonepe.columbus.elastic7.storage.factory.ElasticClientProvider;
import com.phonepe.columbus.elastic7.storage.toolkit.ElasticToolkitInitializer;
import com.phonepe.columbus.registry.factory.RegistryKey;

/**
 * An {@link ElasticClientProvider} that is used to create
 * {@link com.phonepe.columbus.elastic7.internal.storage.ColumbusElasticClient ColumbusElasticClient}
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
