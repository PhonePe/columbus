package com.phonepe.columbus.storage.factory.visitor;

import com.phonepe.columbus.storage.StorageClient;
import com.phonepe.columbus.storage.StorageConfiguration;
import com.phonepe.columbus.storage.factory.StorageClientFactory;
import com.phonepe.columbus.visitor.impl.VisitorContext;
import lombok.experimental.SuperBuilder;

/**
 * A factory to create {@link StorageClient}s that uses {@link com.phonepe.columbus.visitor.Visitor Visitor}
 * construct. Each configuration is expected to have a {@link StorageClientProvider} to create a {@link StorageClient}.
 *
 * @param <C> self reference, the context used by the providers.
 * @implNote The subclasses can provide any additional functionality to be used by the {@link StorageClientProvider}.
 * @see StorageClientProvider
 */
@SuperBuilder(toBuilder = true)
public abstract class StorageClientFactoryContext<C extends StorageClientFactoryContext<C>>
        extends VisitorContext<StorageClient, StorageConfiguration, Void, C> implements StorageClientFactory {

    @Override
    public StorageClient create(final StorageConfiguration configuration) {
        return visit(configuration);
    }
}
