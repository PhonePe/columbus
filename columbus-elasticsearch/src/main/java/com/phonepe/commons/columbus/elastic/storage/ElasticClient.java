package com.phonepe.commons.columbus.elastic.storage;

import com.phonepe.commons.columbus.elastic.storage.configuration.ElasticConfiguration;
import com.phonepe.commons.columbus.elastic.storage.toolkit.ElasticToolkit;
import com.phonepe.commons.columbus.storage.AbstractStorageClient;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;
import org.elasticsearch.client.RestHighLevelClient;

/**
 * An abstract implementation of {@link com.phonepe.commons.columbus.storage.StorageClient StorageClient} that is
 * used by all variants of ElasticSearch.
 *
 * @param <E> the type of the Elastic client.
 */
@Getter
@Accessors(fluent = true)
@SuperBuilder(toBuilder = true)
public abstract class ElasticClient<E extends ElasticClient<E>> extends AbstractStorageClient<ElasticConfiguration, E> {

    @NonNull
    private final RestHighLevelClient client;

    @NonNull
    private final ElasticToolkit toolkit;
}
