package com.phonepe.commons.columbus.elastic.internal.storage.factory.initializer;

import com.phonepe.commons.columbus.common.initializer.Initializer;
import com.phonepe.commons.columbus.common.initializer.LateInit;
import com.phonepe.commons.columbus.common.initializer.LazyInit;
import com.phonepe.commons.columbus.core.toolkit.Toolkit;
import com.phonepe.commons.columbus.elastic.storage.ElasticClient;
import com.phonepe.commons.columbus.elastic.storage.configuration.ElasticConfiguration;
import com.phonepe.commons.columbus.elastic.storage.factory.ElasticClientInitializer;
import com.phonepe.commons.columbus.elastic.storage.toolkit.ElasticToolkit;
import com.phonepe.commons.columbus.elastic.storage.toolkit.ElasticToolkitInitializer;
import com.phonepe.commons.columbus.storage.settings.StorageSettings;
import org.elasticsearch.client.RestHighLevelClient;

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

    private final LazyInit<RestHighLevelClient> restClient =
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

    public Initializer<RestHighLevelClient> getRestClient() {
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

    protected abstract RestHighLevelClient initializeRestClient();

    protected abstract boolean canInitializeElasticToolkit();

    protected abstract ElasticToolkit initializeElasticToolkit();

    protected abstract boolean canInitializeElasticClient();

    @SuppressWarnings("java:S1452")
    protected abstract ElasticClient<?> initializeElasticClient();
}
