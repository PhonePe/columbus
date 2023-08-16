package com.phonepe.platform.columbus.elastic.storage.handlers;

import com.phonepe.platform.columbus.elastic.storage.ElasticClient;
import com.phonepe.platform.columbus.registry.factory.RegistryKey;
import com.phonepe.platform.columbus.storage.commands.VersionCommand;
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

    private String version(final E storage) throws IOException {
        return storage.client().info(RequestOptions.DEFAULT).getVersion().getNumber();
    }
}
