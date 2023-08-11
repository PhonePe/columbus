package com.phonepe.platform.columbus;

import com.phonepe.platform.columbus.core.toolkit.Toolkit;
import com.phonepe.platform.columbus.storage.StorageClient;

import java.util.Collection;

/**
 * A class that represents the runtime state of Columbus.
 */
public interface Columbus {

    /**
     * A toolkit is used to set up all the components of Columbus. It can be configured by providing a
     * {@link com.phonepe.platform.columbus.core.toolkit.ToolkitSettings ToolkitSettings} to
     * {@link com.phonepe.platform.columbus.core.settings.ColumbusSettings ColumbusSettings} during initialization.
     *
     * @return the initialized toolkit.
     */
    Toolkit toolkit();

    /**
     * All the storages that are enabled and initialized are registered here.
     *
     * @return a collection of initialized storages.
     */
    Collection<StorageClient> storages();

    /**
     * Get a storage client by its name.
     *
     * @param name the name of the storage to fetch.
     * @return the storage client with the given name, or null if no matching storage is found.
     */
    StorageClient storage(String name);
}
