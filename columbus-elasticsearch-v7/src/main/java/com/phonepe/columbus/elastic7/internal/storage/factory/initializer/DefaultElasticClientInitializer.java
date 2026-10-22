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
package com.phonepe.columbus.elastic7.internal.storage.factory.initializer;

import com.phonepe.columbus.command.CommandHelpers;
import com.phonepe.columbus.core.utils.RegistryCreators;
import com.phonepe.columbus.elastic7.internal.storage.ColumbusElasticClient;
import com.phonepe.columbus.elastic7.internal.storage.utils.ElasticRestClientUtils;
import com.phonepe.columbus.elastic7.storage.ElasticClient;
import com.phonepe.columbus.elastic7.storage.toolkit.ElasticToolkit;
import com.phonepe.columbus.reflect.Generics;
import com.phonepe.columbus.storage.StorageCommandHandler;
import org.elasticsearch.client.RestHighLevelClient;

/**
 * This class defines the default behaviour for creating the {@link ElasticClient}.
 */
public class DefaultElasticClientInitializer extends LazyElasticClientInitializer {

    @Override
    protected boolean canInitializeRestClient() {
        return getConfiguration().isPresent();
    }

    @Override
    protected RestHighLevelClient initializeRestClient() {
        return ElasticRestClientUtils.createClient(getConfiguration().get());
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
