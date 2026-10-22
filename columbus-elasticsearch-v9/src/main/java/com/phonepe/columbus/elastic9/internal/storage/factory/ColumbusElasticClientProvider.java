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
package com.phonepe.columbus.elastic9.internal.storage.factory;

import com.phonepe.columbus.elastic9.internal.storage.factory.initializer.DefaultElasticClientInitializer;
import com.phonepe.columbus.elastic9.internal.storage.toolkit.DefaultElasticToolkitInitializer;
import com.phonepe.columbus.elastic9.storage.configuration.ElasticConfiguration;
import com.phonepe.columbus.elastic9.storage.factory.ElasticClientInitializer;
import com.phonepe.columbus.elastic9.storage.factory.ElasticClientProvider;
import com.phonepe.columbus.elastic9.storage.toolkit.ElasticToolkitInitializer;
import com.phonepe.columbus.registry.factory.RegistryKey;
import com.phonepe.columbus.storage.factory.impl.ColumbusStorageFactoryContext;

/**
 * An {@link ElasticClientProvider} that is used to create
 * {@link com.phonepe.columbus.elastic9.internal.storage.ColumbusElasticClient ColumbusElasticClient}
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
