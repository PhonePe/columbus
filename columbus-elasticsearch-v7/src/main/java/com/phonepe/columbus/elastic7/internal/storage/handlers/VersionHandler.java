package com.phonepe.columbus.elastic7.internal.storage.handlers;

import com.phonepe.columbus.elastic7.common.Constants.Operations;
import com.phonepe.columbus.elastic7.storage.ElasticClient;
import com.phonepe.columbus.elastic7.storage.handlers.ElasticStorageCommandHandler;
import com.phonepe.columbus.registry.factory.RegistryKey;
import com.phonepe.columbus.storage.commands.VersionCommand;
import io.appform.functionmetrics.MonitoredFunction;
import lombok.SneakyThrows;
import org.elasticsearch.client.RequestOptions;

import java.io.IOException;

/**
 * Get the version of the ElasticSearch cluster.
 *
 * @param <E> the type of the Elastic client.
 */
@RegistryKey(name = VersionCommand.COMMAND_NAME, type = VersionCommand.class)
public class VersionHandler<E extends ElasticClient<E>>
        implements ElasticStorageCommandHandler<String, VersionCommand, E> {

    @Override
    @SneakyThrows
    public String handle(final VersionCommand command, final E storage) {
        return version(storage);
    }

    @MonitoredFunction(className = Operations.CLIENT_NAME, method = Operations.VERSION)
    private String version(final E storage) throws IOException {
        return storage.client().info(RequestOptions.DEFAULT).getVersion().getNumber();
    }
}
