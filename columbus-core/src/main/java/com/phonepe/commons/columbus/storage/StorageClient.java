package com.phonepe.commons.columbus.storage;

import com.phonepe.commons.columbus.command.Executor;
import lombok.NonNull;

/**
 * A storage client is responsible for all interactions with storage.
 * <p>
 * The different operations supported by storage are modelled as
 * {@link com.phonepe.commons.columbus.command.Command Command}s, requiring this class to be an {@link Executor}.
 */
public interface StorageClient extends Executor {

    /**
     * Each storage implementation is associated with a unique type.
     *
     * @return the type of the storage.
     */
    @NonNull
    String type();

    /**
     * Each storage instance is identified by a unique name.
     *
     * @return the name of the storage.
     */
    @NonNull
    String name();
}
