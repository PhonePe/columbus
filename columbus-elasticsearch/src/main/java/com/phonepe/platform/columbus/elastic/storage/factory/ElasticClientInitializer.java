package com.phonepe.platform.columbus.elastic.storage.factory;

import com.phonepe.platform.columbus.core.toolkit.Toolkit;
import com.phonepe.platform.columbus.elastic.storage.ElasticClient;
import com.phonepe.platform.columbus.elastic.storage.configuration.ElasticConfiguration;
import com.phonepe.platform.columbus.elastic.storage.toolkit.ElasticToolkitInitializer;
import com.phonepe.platform.columbus.storage.settings.StorageSettings;
import io.dropwizard.setup.Environment;

/**
 * An interface to initialize {@link ElasticClient} using the different, possibly optional, attributes it needs.
 *
 * @implSpec The attributes are provided as and when they are available, and the object is expected to be created
 * lazily on calling {@link #get()}.
 * @implNote Most likely, an instance of this class can only be used once due to the stateful nature.
 */
public interface ElasticClientInitializer {

    void use(ElasticConfiguration configuration);

    void use(Toolkit toolkit);

    void use(StorageSettings settings);

    void use(Environment environment);

    void use(ElasticToolkitInitializer toolkitInitializer);

    @SuppressWarnings("java:S1452")
    ElasticClient<?> get();
}
