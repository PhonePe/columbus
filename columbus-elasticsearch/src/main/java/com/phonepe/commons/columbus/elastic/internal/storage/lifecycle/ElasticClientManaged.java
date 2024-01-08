package com.phonepe.commons.columbus.elastic.internal.storage.lifecycle;

import io.dropwizard.lifecycle.Managed;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.elasticsearch.client.RestHighLevelClient;

/**
 * An object to automatically close ElasticSearch client on exit.
 */
@AllArgsConstructor
public class ElasticClientManaged implements Managed {

    @NonNull
    private final RestHighLevelClient client;

    @Override
    public void start() {
        // Clients are already started
    }

    @Override
    public void stop() throws Exception {
        client.close();
    }
}
