package com.phonepe.commons.columbus.storage.factory;

import com.phonepe.commons.columbus.storage.StorageClient;
import com.phonepe.commons.columbus.storage.StorageConfiguration;

/**
 * A factory to create a {@link StorageClient} with the given {@link StorageConfiguration}.
 */
public interface StorageClientFactory {

    StorageClient create(StorageConfiguration configuration);
}
