/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.phonepe.columbus.elastic7.storage;

import com.phonepe.columbus.elastic7.storage.configuration.ElasticConfiguration;
import com.phonepe.columbus.elastic7.storage.toolkit.ElasticToolkit;
import com.phonepe.columbus.storage.AbstractStorageClient;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;
import org.elasticsearch.client.RestHighLevelClient;

/**
 * An abstract implementation of {@link com.phonepe.columbus.storage.StorageClient StorageClient} that is
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
