package com.phonepe.platform.columbus.elastic.internal.storage.factory.initializer;

import com.phonepe.platform.columbus.common.initializer.Initializer;
import com.phonepe.platform.columbus.common.initializer.LateInit;
import com.phonepe.platform.columbus.common.initializer.LazyInit;
import com.phonepe.platform.columbus.core.toolkit.Toolkit;
import com.phonepe.platform.columbus.elastic.storage.ElasticClient;
import com.phonepe.platform.columbus.elastic.storage.configuration.ElasticConfiguration;
import com.phonepe.platform.columbus.elastic.storage.factory.ElasticClientInitializer;
import com.phonepe.platform.columbus.storage.settings.StorageSettings;
import io.dropwizard.setup.Environment;
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
    private final LateInit<Environment> environment = new LateInit<>("environment");

    private final LazyInit<RestHighLevelClient> restClient =
            new LazyInit<>("restClient", this::canInitializeRestClient, this::initializeRestClient);

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
    public void use(final Environment environment) {
        this.environment.set(environment);
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

    public Initializer<Environment> getEnvironment() {
        return environment;
    }

    public Initializer<RestHighLevelClient> getRestClient() {
        return restClient;
    }

    @SuppressWarnings("java:S1452")
    public Initializer<ElasticClient<?>> getElasticClient() {
        return elasticClient;
    }

    protected abstract boolean canInitializeRestClient();

    protected abstract RestHighLevelClient initializeRestClient();

    protected abstract boolean canInitializeElasticClient();

    @SuppressWarnings("java:S1452")
    protected abstract ElasticClient<?> initializeElasticClient();
}
