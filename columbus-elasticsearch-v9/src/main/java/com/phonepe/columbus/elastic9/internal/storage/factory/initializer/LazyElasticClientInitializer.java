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
package com.phonepe.columbus.elastic9.internal.storage.factory.initializer;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import com.phonepe.columbus.common.initializer.Initializer;
import com.phonepe.columbus.common.initializer.LateInit;
import com.phonepe.columbus.common.initializer.LazyInit;
import com.phonepe.columbus.core.toolkit.Toolkit;
import com.phonepe.columbus.elastic9.storage.ElasticClient;
import com.phonepe.columbus.elastic9.storage.configuration.ElasticConfiguration;
import com.phonepe.columbus.elastic9.storage.factory.ElasticClientInitializer;
import com.phonepe.columbus.elastic9.storage.toolkit.ElasticToolkit;
import com.phonepe.columbus.elastic9.storage.toolkit.ElasticToolkitInitializer;
import com.phonepe.columbus.storage.settings.StorageSettings;

/**
 * An object that uses the different {@link Initializer}s to create a dependency graph between the fields of
 * {@link ElasticClient}. The preconditions and initializers are exposed to allow the children to define the
 * behaviour for each of the fields independently.
 *
 * @see DefaultElasticClientInitializer
 */
public abstract class LazyElasticClientInitializer implements ElasticClientInitializer {

    private final LateInit<ElasticConfiguration> configuration = new LateInit<>("configuration");
    private final LateInit<Toolkit> toolkit = new LateInit<>("toolkit");
    private final LateInit<StorageSettings> settings = new LateInit<>("settings");
    private final LateInit<ElasticToolkitInitializer> elasticToolkitInitializer =
            new LateInit<>("elasticToolkitInitializer");

    private final LazyInit<ElasticsearchClient> restClient =
            new LazyInit<>("restClient", this::canInitializeRestClient, this::initializeRestClient);
    private final LazyInit<ElasticToolkit> elasticToolkit =
            new LazyInit<>("elasticToolkit", this::canInitializeElasticToolkit, this::initializeElasticToolkit);

    private final LazyInit<ElasticClient<?>> elasticClient =
            new LazyInit<>("elasticClient", this::canInitializeElasticClient, this::initializeElasticClient);

    @Override
    public void use(final ElasticConfiguration configuration) {
        this.configuration.set(configuration);
    }

    @Override
    public void use(final Toolkit toolkit) {
        this.toolkit.set(toolkit);
    }

    @Override
    public void use(final StorageSettings settings) {
        this.settings.set(settings);
    }

    @Override
    public void use(final ElasticToolkitInitializer toolkitInitializer) {
        elasticToolkitInitializer.set(toolkitInitializer);
    }

    @Override
    public ElasticClient<?> get() {
        return getElasticClient().get();
    }

    public Initializer<ElasticConfiguration> getConfiguration() {
        return configuration;
    }

    public Initializer<Toolkit> getToolkit() {
        return toolkit;
    }

    public Initializer<StorageSettings> getSettings() {
        return settings;
    }

    public Initializer<ElasticToolkitInitializer> getElasticToolkitInitializer() {
        return elasticToolkitInitializer;
    }

    public Initializer<ElasticsearchClient> getRestClient() {
        return restClient;
    }

    public Initializer<ElasticToolkit> getElasticToolkit() {
        return elasticToolkit;
    }

    @SuppressWarnings("java:S1452")
    public Initializer<ElasticClient<?>> getElasticClient() {
        return elasticClient;
    }

    protected abstract boolean canInitializeRestClient();

    protected abstract ElasticsearchClient initializeRestClient();

    protected abstract boolean canInitializeElasticToolkit();

    protected abstract ElasticToolkit initializeElasticToolkit();

    protected abstract boolean canInitializeElasticClient();

    @SuppressWarnings("java:S1452")
    protected abstract ElasticClient<?> initializeElasticClient();
}
