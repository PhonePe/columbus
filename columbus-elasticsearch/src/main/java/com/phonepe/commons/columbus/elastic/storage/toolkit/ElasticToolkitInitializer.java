package com.phonepe.commons.columbus.elastic.storage.toolkit;

import com.phonepe.commons.columbus.core.toolkit.Toolkit;
import com.phonepe.commons.columbus.elastic.storage.configuration.ElasticConfiguration;
import com.phonepe.commons.columbus.storage.settings.StorageSettings;

/**
 * An interface to initialize {@link ElasticToolkit} using the different, possibly optional, attributes it needs.
 *
 * @implSpec The attributes are provided as and when they are available, and the object is expected to be created
 * lazily on calling {@link #get()}.
 * @implNote Most likely, an instance of this class can only be used once due to the stateful nature.
 */
public interface ElasticToolkitInitializer {

    void use(Toolkit toolkit);

    void use(StorageSettings storageSettings);

    void use(ElasticConfiguration configuration);

    ElasticToolkit get();
}
