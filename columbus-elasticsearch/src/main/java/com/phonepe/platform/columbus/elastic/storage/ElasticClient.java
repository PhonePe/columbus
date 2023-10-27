package com.phonepe.platform.columbus.elastic.storage;

import com.phonepe.platform.columbus.elastic.storage.configuration.ElasticConfiguration;
import com.phonepe.platform.columbus.elastic.storage.toolkit.ElasticToolkit;
import com.phonepe.platform.columbus.storage.BaseStorageClient;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;
import org.elasticsearch.client.RestHighLevelClient;

/**
 * A base implementation of {@link com.phonepe.platform.columbus.storage.StorageClient StorageClient} that is used by
 * all variants of ElasticSearch.
 *
 * @param <E> the type of the Elastic client.
 */
@Getter
@Accessors(fluent = true)
@SuperBuilder(toBuilder = true)
public abstract class ElasticClient<E extends ElasticClient<E>> extends BaseStorageClient<ElasticConfiguration, E> {

    @NonNull
    private final RestHighLevelClient client;

    @NonNull
    private final ElasticToolkit toolkit;
}
