package com.phonepe.platform.columbus.storage.factory;

import com.phonepe.platform.columbus.storage.StorageClient;
import com.phonepe.platform.columbus.storage.StorageConfiguration;

/**
 * A factory to create a {@link StorageClient} with the given {@link StorageConfiguration}.
 */
public interface StorageClientFactory {

    StorageClient create(StorageConfiguration configuration);
}
