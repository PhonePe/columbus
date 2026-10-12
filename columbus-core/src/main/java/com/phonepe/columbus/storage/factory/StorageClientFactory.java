package com.phonepe.columbus.storage.factory;

import com.phonepe.columbus.storage.StorageClient;
import com.phonepe.columbus.storage.StorageConfiguration;

/**
 * A factory to create a {@link StorageClient} with the given {@link StorageConfiguration}.
 */
public interface StorageClientFactory {

    StorageClient create(StorageConfiguration configuration);
}
