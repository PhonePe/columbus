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
package com.phonepe.columbus.elastic9.storage.factory;

import com.phonepe.columbus.elastic9.storage.configuration.ElasticConfiguration;
import com.phonepe.columbus.elastic9.storage.toolkit.ElasticToolkitInitializer;
import com.phonepe.columbus.storage.StorageClient;
import com.phonepe.columbus.storage.factory.impl.ColumbusStorageFactoryContext;
import com.phonepe.columbus.storage.factory.visitor.StorageClientProvider;

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
