package com.phonepe.commons.columbus.elastic9.internal.storage.lifecycle;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import io.dropwizard.lifecycle.Managed;
import lombok.AllArgsConstructor;
import lombok.NonNull;

/**
 * An object to automatically close ElasticSearch client on exit.
 */
@AllArgsConstructor
public class ElasticClientManaged implements Managed {

    @NonNull
    private final ElasticsearchClient client;

    @Override
    public void start() {
        // Clients are already started
    }

    @Override
    public void stop() throws Exception {
        client.close();
    }
}
